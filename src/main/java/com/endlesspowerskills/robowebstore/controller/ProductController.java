package com.endlesspowerskills.robowebstore.controller;

import com.endlesspowerskills.robowebstore.entity.Product;
import com.endlesspowerskills.robowebstore.service.ProductService;
import com.endlesspowerskills.robowebstore.util.AttributeNames;
import com.endlesspowerskills.robowebstore.util.PageMappings;
import com.endlesspowerskills.robowebstore.util.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(PageMappings.ROBOWEBSTORE)
public class ProductController {

    // -- fields
    private final ProductService productService;

    // -- constructors
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // -- methods
    @GetMapping(PageMappings.PRODUCTS)
    public String products(Model model){
        Iterable<Product> products = productService.findAll();
        model.addAttribute(AttributeNames.PRODUCTS, products);
        return ViewNames.PRODUCTS;
    }


//    @GetMapping(PageMappings.PRODUCTS + PageMappings.PRODUCT + "/{id}")
//    public String productDetail(@PathVariable Integer id, Model model){
//        model.addAttribute("product", productService.findById(id));
//        return ViewNames.PRODUCT;
//    }

    @GetMapping(PageMappings.PRODUCTS + PageMappings.PRODUCT)
    public String productDetail(@RequestParam("id") Integer id, Model model){
        model.addAttribute(AttributeNames.PRODUCT, productService.findById(id));
        return ViewNames.PRODUCT;
    }

//    @GetMapping(PageMappings.PRODUCTS + PageMappings.FILTER)
//    public String getProductsByCategory(@RequestParam("category") String category, Model model){
//        model.addAttribute(AttributeNames.PRODUCTS, productService.findAllByCategory(category));
//        return ViewNames.PRODUCTS;
//    }

    @RequestMapping(PageMappings.PRODUCTS + PageMappings.FILTER + "/{byCriteria}")
    public String getProductsByCriteria(@MatrixVariable(pathVar = "byCriteria") Map<String, List<String>> filterParams, Model model){
        log.info("fiterParams key: {}", filterParams.keySet());
        model.addAttribute(AttributeNames.PRODUCTS, productService.getProductsByFilter(filterParams));
        return ViewNames.PRODUCTS;
    }

    @GetMapping(PageMappings.ADD_PRODUCT)
    public String addProduct(Model model){
        Product product = new Product();
        model.addAttribute(AttributeNames.NEW_PRODUCT, product);
        return ViewNames.ADD_PRODUCT;
    }

    @PostMapping(PageMappings.ADD_PRODUCT)
    public String processAddProduct(@ModelAttribute(AttributeNames.NEW_PRODUCT) Product newProduct, @RequestParam String name,
                                    @RequestParam double price, @RequestParam String description, @RequestParam String manufacturer,
                                    @RequestParam String category, @RequestParam int unitsInStock, @RequestParam MultipartFile multipartFile){
        newProduct.setName(name);
        newProduct.setPrice(price);
        newProduct.setDescription(description);
        newProduct.setManufacturer(manufacturer);
        newProduct.setCategory(category);
        newProduct.setUnitsInStock(unitsInStock);
        try{
            newProduct.setImage(Base64Utils.encodeToString(multipartFile.getBytes()));
        } catch (IOException e) {
            log.debug("Encode to string exception: {}", e.getMessage());
            e.printStackTrace();
        }
        productService.save(newProduct);
        return ViewNames.ADD_PRODUCT;
    }
}
