package com.example.wegomarket.repository;

import com.example.wegomarket.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findById(long id);
    List<Product> findByName(String name);
    void deleteById(long id);
}
