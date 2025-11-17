package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.exceptions.CartItemNotFoundException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    public CartDto createCart(){
        Cart cart = cartRepository.save(new Cart());
        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productID){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
           throw new CartNotFoundException();

        Product product = productRepository.findById(productID).orElse(null);
        if (product == null)
           throw new ProductNotFoundException();

        CartItem item = cart.addProduct(product);
        cartRepository.save(cart);

        return cartMapper.toDto(item);
    }

    public CartDto getCart(UUID cartId){
        Cart cart = cartRepository.findById(cartId).orElse(null);

        if(cart == null)
            throw new CartNotFoundException();
        return (cartMapper.toDto(cart));
    }

    public CartItemDto updateProductQuantity(UUID cartId, long productId, int quantity ){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null)
            throw new CartNotFoundException();

        CartItem cartItem = cart.getCartItem(productId);

        if(cartItem == null)
            throw new CartItemNotFoundException();

        cartItem.setQuantity(quantity);

        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void deleteItem(UUID cartId, long productId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null)
           throw new CartNotFoundException();

        cart.deleteCartItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null)
           throw new CartNotFoundException();

        cart.clearCart();
//        cartRepository.delete(cart); // this is deleting the cart instead we need to clear
        cartRepository.save(cart);

    }

}
