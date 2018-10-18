package com.endlesspowerskills.robowebstore.service;

import com.endlesspowerskills.robowebstore.entity.Product;
import com.endlesspowerskills.robowebstore.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    // -- fields
    private final ProductRepository productRepository;

    // -- constructors
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // -- methods
    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findById(id).get();
    }

    @Override
    public List<Product> findAllByCategory(String category) {
        return productRepository.findAllByCategory(category);
    }

    @Override
    public Set<Product> getProductsByFilter(Map<String, List<String>> filterParams) {
        Set<Product> filteredProducts = new HashSet<>();
        Set<String> criteria = filterParams.keySet();
        int criteriaFound = 0;
        String[] defaultCriteria = {"manufacturer", "category"};
        for(String criteriaName : defaultCriteria){
            if(criteria.contains(criteriaName)){
                criteriaFound++;
                Set<Product> tempSetProducts = new HashSet<>();
                for(String filter : filterParams.get(criteriaName)){
                    switch (criteriaName){
                        case "manufacturer":
                            tempSetProducts.addAll(productRepository.findAllByManufacturer(filter));
                            break;
                        case "category":
                            tempSetProducts.addAll(productRepository.findAllByCategory(filter));
                            break;
                    }
                    if(criteriaFound == 1){
                        log.info("{} criteriaFound, tempSetproduct: {}", criteriaFound, tempSetProducts);
                        filteredProducts.addAll(tempSetProducts);
                        log.info("{} criteriaFound, filteredProducts: {}", criteriaFound, filteredProducts);
                    } else if(criteriaFound > 1){
                        log.info("{} criteriaFound, tempSetproduct: {}", criteriaFound, tempSetProducts);
                        filteredProducts.retainAll(tempSetProducts);
                        log.info("{} criteriaFound, filteredProducts: {}", criteriaFound, filteredProducts);
                    }
                }

            }
        }
        if(criteria.contains("low") || criteria.contains("high")){
            if(criteriaFound == 0){
                filteredProducts.addAll(priceFilter(filterParams));
            } else {
                filteredProducts.retainAll(priceFilter(filterParams));
            }
        }
        return filteredProducts;
    }

    private List<Product> priceFilter(Map<String, List<String>> filterParams) {
        double low = 0;
        double high = Double.MAX_VALUE;
        if(filterParams.containsKey("low")){
            try{
                low = Double.parseDouble(filterParams.get("low").get(0));
            } catch (NumberFormatException e){
                log.error("Wrong format of price low: {}", e.getMessage());
            }
        }
        if(filterParams.containsKey("high")){
            try{
                high = Double.parseDouble(filterParams.get("high").get(0));
            } catch (NumberFormatException e){
                log.error("Wrong format of price high: {}", e.getMessage());
            }
        }
        log.info("Low = {}, high = {}", low, high);
        return productRepository.findProductsByPriceIsBetween(low, high);
    }


}
