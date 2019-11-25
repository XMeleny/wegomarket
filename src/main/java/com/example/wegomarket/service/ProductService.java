package com.example.wegomarket.service;

import com.example.wegomarket.model.Product;
import java.util.List;

public interface ProductService {
    public List<Product> getProductList();

    public Product findProductById(long id);

    public List<Product> findProductByName(String name);

    public void save(Product product);

    public void edit(Product product);

    public void delete(long id);
}
