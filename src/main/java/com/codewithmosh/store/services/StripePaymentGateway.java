package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderItem;
import com.codewithmosh.store.entities.OrderStatus;
import com.codewithmosh.store.exceptions.PaymentException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway {

    @Value("${webUrl}")
    private String webUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try{
            SessionCreateParams.Builder builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(webUrl + "/checkout-success?orderId=" + order.getId())
                .setCancelUrl(webUrl + "/checkout-cancel")
                .setPaymentIntentData(setMetaData(order));

            order.getOrderItems().forEach(item -> {
                var lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        }catch (StripeException e){
            System.out.println(e.getMessage());
            throw new PaymentException();
        }

    }

    private static SessionCreateParams.PaymentIntentData setMetaData(Order order) {
        return SessionCreateParams.PaymentIntentData.builder()
                .putMetadata("order_id", order.getId().toString()).build();
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
       return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("inr")
                .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                .setProductData(createProductData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .build();
    }

    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {
            Event event = Webhook.constructEvent(request.getPayload(), request.getHeaders().get("stripe-signature"), webhookSecretKey);

            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    //update order status as paid
                  return Optional.of(new PaymentResult(extractOrderId(event), OrderStatus.PAID));
                }
                case "payment_intent.payment_failed" -> {
                    //update order status as failed
                    return Optional.of(new PaymentResult(extractOrderId(event), OrderStatus.FAILED));
                }
                default -> {
                    return Optional.empty();
                }
            }
        } catch (SignatureVerificationException e) {
            throw new PaymentException("Exception with the stripe");
        }
    }

    public Long extractOrderId(Event event){
        StripeObject stripeObject = event.getDataObjectDeserializer().getObject()
                .orElseThrow(()-> new PaymentException("Couldn't deserializer stripe event, check the SDK and API version"));
        PaymentIntent paymentIntent = (PaymentIntent) stripeObject;

            String orderId = paymentIntent.getMetadata().get("order_id");
            return Long.valueOf(orderId);

    }
}
