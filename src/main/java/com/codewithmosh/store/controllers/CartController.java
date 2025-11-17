package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.*;
import com.codewithmosh.store.exceptions.CartItemNotFoundException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.repositories.CartItemRepository;
import com.codewithmosh.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Cart")
public class CartController {
   final CartItemRepository cartItemRepository;
    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        var cartDto = cartService.createCart();

        URI uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Adding product to a cart")
    public ResponseEntity<CartItemDto> addItemToCart(
            @Parameter(description = "Enter Cart Id")
            @PathVariable(name = "cartId") UUID cartId,
            @RequestBody AddProductToCartRequest request
    ) {
        var cartItemDto = cartService.addToCart(cartId, request.getProductId());
        return new ResponseEntity<>(cartItemDto, HttpStatus.CREATED ) ;

    }

    @GetMapping("/{cartId}")
    public CartDto getCart(
            @PathVariable(name = "cartId") UUID cartId
    ){
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateProductQuantity(
            @Valid @RequestBody UpdateProductQuantityRequest request,
            @PathVariable(name= "cartId") UUID cartId,
            @PathVariable(name = "productId") long productId
            ){
        //by default the status is 200 as we are not changing the status we can return the same instead of Response Entity
       return cartService.updateProductQuantity(cartId, productId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteProductFromCart(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") long productId
    ){
        cartService.deleteItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable(name = "cartId") UUID cartId){
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                //Map.of("Error: ", "Cart Not Found")
                new ErrorDto("Cart Not Found")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                //Map.of("Error: ", "Product Not Found")
                new ErrorDto("Product Not Found")
        );
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartItemNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                //Map.of("Error: ", "CartItem not found")
                new ErrorDto("CartItem not found")
        );
    }
}
