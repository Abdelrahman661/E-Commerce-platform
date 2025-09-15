package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.CategoryDto;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository repo;
    public CategoryService(CategoryRepository repo){ this.repo = repo; }

    public CategoryDto create(CategoryDto dto){
        Category c = new Category();
        c.setName(dto.getName());
        Category saved = repo.save(c);
        dto.setId(saved.getId());
        return dto;
    }

    public List<CategoryDto> list(){
        return repo.findAll().stream().map(c -> {
            CategoryDto d = new CategoryDto();
            d.setId(c.getId());
            d.setName(c.getName());
            return d;
        }).collect(Collectors.toList());
    }
}