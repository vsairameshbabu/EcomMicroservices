package com.app.ecom.controller;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;
import com.app.ecom.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-Id") String userId,
            @RequestBody CartItemRequest cartItemRequest) {
        if (!cartItemService.addToCart(userId, cartItemRequest)) {
            return ResponseEntity.badRequest().body("Product not found or out of stock or invalid UserId");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("items/{productId}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-Id") String userId,
                                               @PathVariable Long productId) {
        boolean deleted = cartItemService.deleteItemFromCart(userId, productId);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();

    }

    @GetMapping()
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-Id") String userId) {
        return new ResponseEntity<>(cartItemService.getCart(userId), HttpStatus.OK);
    }
}
