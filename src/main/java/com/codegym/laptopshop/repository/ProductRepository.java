package com.codegym.laptopshop.repository;

import com.codegym.laptopshop.entity.Category;
import com.codegym.laptopshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Iterable<Product> findByActivated(Boolean isActivated);
    Iterable<Product> findByNameContainingAndActivated(String keyword, Boolean isActivated);
    Iterable<Product> findByCategory(Category category);
    Page<Product> findAllByNameContaining(String fullName, Pageable pageable);
    Page<Product> findAllByNameContainingAndActivated(String fullName, Boolean isActivated, Pageable pageable);

    Page<Product> findAll(Pageable pageable);
    Optional<Product> findByName(String name);
}
