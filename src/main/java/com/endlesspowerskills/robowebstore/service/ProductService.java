package com.endlesspowerskills.robowebstore.service;

import com.endlesspowerskills.robowebstore.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductService {
    void save(Product product);
    Iterable<Product> findAll();
    Product findById(Integer id);
    List<Product> findAllByCategory(String category);
    Set<Product> getProductsByFilter(Map<String, List<String>> filterParams);
}
