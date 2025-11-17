package com.codewithmosh.store.entities;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;


    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CartItem> items = new LinkedHashSet();

    public BigDecimal getTotalPrice(){
         BigDecimal totalPrice = BigDecimal.ZERO;
         /*items.forEach(item -> totalPrice.add(item.getTotalPrice()));
         return totalPrice;*/

        for(CartItem item: items){
            totalPrice = totalPrice.add(item.getTotalPrice());
            System.out.println(item.getTotalPrice()+"========================");
        }
        return totalPrice;
    }

    public CartItem getCartItem(long productId){
        return items.stream().filter(item -> item.getProduct().getId()== productId).findFirst().orElse(null);
    }

    public CartItem addProduct(Product product){
        CartItem cartItem = getCartItem(product.getId());
        if(cartItem == null){
            CartItem item = CartItem.builder()
                    .product(product)
                    .quantity(1)
                    .cart(this)
                    .build();
           items.add(item);
        }else{
            cartItem.setQuantity(cartItem.getQuantity()+1);
        }
        return cartItem;
    }


    public void deleteCartItem(Long productId){
        CartItem cartItem = getCartItem(productId);
        if(cartItem != null){
            items.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public void clearCart(){
        items.clear();
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }


}
