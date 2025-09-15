package com.ecommerce.order.dto;

import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long orderId;
    private String status;
    private Double totalAmount;
    private OffsetDateTime orderDate;
    private List<Item> items;
    @Data
    public static class Item {
        private Long productId;
        private Integer quantity;
        private Double price;
    }
}
