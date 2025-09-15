package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.security.JwtUtil;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestHeader("Authorization") String authHeader,
                                                    @RequestBody OrderRequest request){
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        if(!jwtUtil.validateToken(token)) return ResponseEntity.status(401).build();
        String userEmail = jwtUtil.extractUsername(token); // subject in token

        OrderResponse resp = orderService.placeOrder(userEmail, request);
        return ResponseEntity.ok(resp);
    }
}
