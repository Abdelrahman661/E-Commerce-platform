package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.entity.OrderEntity;
import com.ecommerce.order.entity.OrderItemEntity;
import com.ecommerce.order.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final RestTemplate rest = new RestTemplate();
    private final String productServiceUrl;
    private final String cartServiceUrl;

    public OrderService(OrderRepository orderRepo,
                        org.springframework.core.env.Environment env){
        this.orderRepo = orderRepo;
        this.productServiceUrl = env.getProperty("external.product-service-url");
        this.cartServiceUrl = env.getProperty("external.cart-service-url");
    }

    @Transactional
    public OrderResponse placeOrder(String userEmail, OrderRequest request){
        double total = 0.0;
        List<OrderItemEntity> items = new ArrayList<>();

        for(OrderRequest.Item it : request.getItems()){
            String url = productServiceUrl + "/api/products/" + it.getProductId();
            ResponseEntity<Map> resp = rest.getForEntity(url, Map.class);
            if(!resp.getStatusCode().is2xxSuccessful()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Product not found");
            Map body = resp.getBody();
            Double price = Double.valueOf(body.get("price").toString());
            Integer stock = Integer.valueOf(body.get("stock").toString());
            if(stock < it.getQuantity()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Insufficient stock for product " + it.getProductId());

            total += price * it.getQuantity();

            rest.postForEntity(productServiceUrl + "/api/products/" + it.getProductId() + "/stock?delta=" + (-it.getQuantity()), null, Void.class);

            OrderItemEntity oi = new OrderItemEntity();
            oi.setProductId(it.getProductId());
            oi.setQuantity(it.getQuantity());
            oi.setPrice(price);
            items.add(oi);
        }

        OrderEntity order = new OrderEntity();
        order.setUserEmail(userEmail);
        order.setOrderDate(OffsetDateTime.now());
        order.setStatus("CREATED");
        order.setTotalAmount(total);
        order.setItems(items);
        items.forEach(i -> i.setOrder(order));

        OrderEntity saved = orderRepo.save(order);


        try {
            rest.postForEntity(cartServiceUrl + "/api/cart/extend?userEmail=" + userEmail, null, Void.class);
        } catch(Exception ex){ /* log but do not rollback order */ }


        OrderResponse resp = new OrderResponse();
        resp.setOrderId(saved.getId());
        resp.setOrderDate(saved.getOrderDate());
        resp.setTotalAmount(saved.getTotalAmount());
        resp.setStatus(saved.getStatus());
        List<OrderResponse.Item> respItems = new ArrayList<>();
        for(OrderItemEntity oi : saved.getItems()){
            OrderResponse.Item i = new OrderResponse.Item();
            i.setProductId(oi.getProductId());
            i.setPrice(oi.getPrice());
            i.setQuantity(oi.getQuantity());
            respItems.add(i);
        }
        resp.setItems(respItems);
        return resp;
    }
}
