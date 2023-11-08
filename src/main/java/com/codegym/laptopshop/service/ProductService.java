package com.codegym.laptopshop.service;

import com.codegym.laptopshop.dto.CategoryDto;
import com.codegym.laptopshop.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ProductService extends GeneralService<ProductDto>{
    Iterable<ProductDto> findByNameContaining(String keyword);
    Iterable<ProductDto> findByCategory(CategoryDto categoryDto);

//    Page<ProductDto> findAllByFullNameContaining(String fullName, Pageable pageable);

    Page<ProductDto> findAll(Pageable pageable);

    Page<ProductDto> findPaginated(int pageNum, int pageSize, String sortField, String sortDirection);

    void deleteByCategory(CategoryDto categoryDto);

    void update(ProductDto productDto);
    Optional<ProductDto> createAndGetProduct(ProductDto productDto);
    Optional<ProductDto> findByName(String name);
}
