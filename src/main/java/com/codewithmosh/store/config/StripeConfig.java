package com.codewithmosh.store.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class StripeConfig {
    @Value("${stripe.secretKey}")
    private String secretKey;



    @PostConstruct
    public void init(){
        Stripe.apiKey = secretKey;
    }

}
