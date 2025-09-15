package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.dto.ProductDto;
import com.ecommerce.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto){
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> get(@PathVariable Long id){
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> list(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(service.list(page,size));
    }

    @PostMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable Long id, @RequestParam int delta){
        service.updateStock(id, delta);
        return ResponseEntity.ok().build();
    }
}