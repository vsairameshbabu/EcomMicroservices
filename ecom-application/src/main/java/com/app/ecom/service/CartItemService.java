package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartItemService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {
        Optional<Product> productOp = productRepository.findById(cartItemRequest.getProductId());
        if(productOp.isEmpty()){
            return false;
        }
        Product product = productOp.get();
        if(product.getStockQuantity() < cartItemRequest.getQuantity()){
            return false;
        }
        Optional<User> userOp = userRepository.findById(Long.parseLong(userId));
        if(userOp.isEmpty()){
            return false;
        }
        User user = userOp.get();

        CartItem existingCartItem = cartItemRepository.findByProductAndUser(product, user);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setUser(user);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        Optional<Product> productOp = productRepository.findById(productId);
        Optional<User> userOp = userRepository.findById(Long.parseLong(userId));
        if(userOp.isPresent() && productOp.isPresent()) {
            cartItemRepository.deleteByUserAndProduct(userOp.get(), productOp.get());
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
       return  userRepository.findById(Long.parseLong(userId))
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of);
    }
}
