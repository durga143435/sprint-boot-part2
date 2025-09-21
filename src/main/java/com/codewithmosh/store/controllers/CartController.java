package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddProductToCartRequest;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {

        Cart cart = cartRepository.save(new Cart());

        URI uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart).toUri();
        return ResponseEntity.created(uri).body(cartMapper.toDto(cart));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addItemToCart(
            @PathVariable(name = "cartId") UUID cartId,
            @RequestBody AddProductToCartRequest request
    ) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
            return ResponseEntity.notFound().build();
        System.out.println("======================cart: "+ cart);
        System.out.println("======================product id : "+ request.getProductId());

        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null)
            return ResponseEntity.badRequest().build();



        for (CartItem i : cart.getItems()) {
            if (i.getProduct().getId() == request.getProductId()) {
                i.setQuantity(i.getQuantity()+1);
                cartRepository.save(cart);
                URI uri = null;
                return ResponseEntity.created(uri).body(cartMapper.toDto(i));
            }
        }

        CartItem item = CartItem.builder()
                .product(product)
                .quantity(1).build();
        cart.getItems().add(item);
        item.setCart(cart);

        cartRepository.save(cart);
        return new ResponseEntity<>(cartMapper.toDto(item), HttpStatus.CREATED ) ;

    }

  /*  @GetMapping("/{cartId}")
    public ResponseEntity<GetACartResponse> getCart(
            @PathVariable(name = "cartId") UUID cartId
    ){
        Cart cart = cartRepository.findById(cartId).orElse(null);

        if(cart == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cartMapper.getCartResponseDto(cart));
    }*/
}
