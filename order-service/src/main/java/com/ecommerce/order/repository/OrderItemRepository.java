package com.ecommerce.order.repository;

import com.ecommerce.order.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    // You can add custom queries if needed later, for example:
    // List<OrderItem> findByOrderId(Long orderId);
}
