package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.dto.CategoryDto;
import com.ecommerce.product_service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto){
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> list(){
        return ResponseEntity.ok(service.list());
    }
}