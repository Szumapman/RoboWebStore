package com.endlesspowerskills.robowebstore.repository;

import com.endlesspowerskills.robowebstore.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    List<Product> findAllByCategory(String category);
    List<Product> findAllByManufacturer(String manufacturer);
    List<Product> findProductsByPriceIsBetween(double low, double high);

}
