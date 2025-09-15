package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductDto;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductService(ProductRepository productRepo, CategoryRepository categoryRepo){
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    public ProductDto create(ProductDto dto){
        Product p = new Product();
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock()==null?0:dto.getStock());
        if(dto.getCategoryId()!=null){
            Category cat = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Category not found"));
            p.setCategory(cat);
        }
        Product saved = productRepo.save(p);
        dto.setId(saved.getId());
        return dto;
    }

    public ProductDto get(Long id){
        Product p = productRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));
        ProductDto d = new ProductDto();
        d.setId(p.getId()); d.setName(p.getName());
        d.setDescription(p.getDescription()); d.setPrice(p.getPrice());
        d.setStock(p.getStock()); if(p.getCategory()!=null) d.setCategoryId(p.getCategory().getId());
        return d;
    }

    public Page<ProductDto> list(int page, int size){
        Page<Product> pg = productRepo.findAll(PageRequest.of(page,size));
        return pg.map(p->{
            ProductDto d = new ProductDto();
            d.setId(p.getId()); d.setName(p.getName()); d.setDescription(p.getDescription());
            d.setPrice(p.getPrice()); d.setStock(p.getStock()); if(p.getCategory()!=null) d.setCategoryId(p.getCategory().getId());
            return d;
        });
    }

    public void updateStock(Long productId, int delta){
        Product p = productRepo.findById(productId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));
        int newStock = p.getStock() + delta;
        if(newStock < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Insufficient stock");
        p.setStock(newStock);
        productRepo.save(p);
    }
}