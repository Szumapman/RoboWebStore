package com.endlesspowerskills.robowebstore.controller;

import com.endlesspowerskills.robowebstore.entity.Product;
import com.endlesspowerskills.robowebstore.service.ProductService;
import com.endlesspowerskills.robowebstore.util.AttributeNames;
import com.endlesspowerskills.robowebstore.util.ParameterValues;
import com.endlesspowerskills.robowebstore.util.PageMappings;
import com.endlesspowerskills.robowebstore.util.ViewNames;
import com.endlesspowerskills.robowebstore.validator.ProductValidator;
import com.endlesspowerskills.robowebstore.validator.ValuableUnitsInStockValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(PageMappings.ROBOWEBSTORE)
public class ProductController {

    // -- fields
    private final ProductService productService;
    private final ProductValidator productValidator;

    // -- constructors
    @Autowired
    public ProductController(ProductService productService, ProductValidator productValidator) {
        this.productService = productService;
        this.productValidator = productValidator;
    }


    // -- methods
    @GetMapping(PageMappings.PRODUCTS)
    public String products(Model model) {
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
    public String productDetail(@RequestParam("id") Integer id, Model model) {
        model.addAttribute(AttributeNames.PRODUCT, productService.findById(id));
        return ViewNames.PRODUCT;
    }

//    @GetMapping(PageMappings.PRODUCTS + PageMappings.FILTER)
//    public String getProductsByCategory(@RequestParam("category") String category, Model model){
//        model.addAttribute(AttributeNames.PRODUCTS, productService.findAllByCategory(category));
//        return ViewNames.PRODUCTS;
//    }

    @RequestMapping(PageMappings.PRODUCTS + PageMappings.FILTER + "/{byCriteria}")
    public String getProductsByCriteria(@MatrixVariable(pathVar = "byCriteria") Map<String, List<String>> filterParams, Model model) {
        log.info("fiterParams key: {}", filterParams.keySet());
        model.addAttribute(AttributeNames.PRODUCTS, productService.getProductsByFilter(filterParams));
        return ViewNames.PRODUCTS;
    }

    @GetMapping( PageMappings.ADD_PRODUCT)
    public String addProduct(@ModelAttribute(AttributeNames.NEW_PRODUCT) Product newProduct) {
        return ViewNames.ADD_PRODUCT;
    }


    @PostMapping(PageMappings.ADD_PRODUCT)
    public String processAddProduct(@ModelAttribute(AttributeNames.NEW_PRODUCT) @Valid Product newProduct, BindingResult result,
                                    @RequestParam MultipartFile multipartFileImage, @RequestParam MultipartFile multipartFileManual) {
        // validation
        if(result.hasErrors()){
            return ViewNames.ADD_PRODUCT;
        }
        // Adding image to database
        try {
            if(multipartFileImage != null && !multipartFileImage.isEmpty()){
                newProduct.setImage(Base64Utils.encodeToString(multipartFileImage.getBytes()));
            }
            if(multipartFileManual != null && !multipartFileManual.isEmpty()){
                newProduct.setManual(multipartFileManual.getBytes());
                newProduct.setFileType(multipartFileManual.getContentType());
                String orgFileName = multipartFileManual.getOriginalFilename();
                newProduct.setFileName(newProduct.getName() + orgFileName.substring(orgFileName.lastIndexOf(".")));
                log.info("Manual name: {}", newProduct.getFileName());
            }
        } catch (IOException e) {
            log.error("Multipartfile exception: {}", e.getMessage());
            e.printStackTrace();
        }

        // Checking binder
        String[] suppressedFields = result.getSuppressedFields();
        if(suppressedFields.length > 0) {
            throw new RuntimeException("Próba wiązania niedozwolonych pól: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
        }

        productService.save(newProduct);
        log.info("New product id: {}", newProduct.getId());
        return "redirect:" + PageMappings.ROBOWEBSTORE + PageMappings.ADD_PRODUCT;
    }

    @InitBinder
    public void initialiseBinder(WebDataBinder binder){
        binder.setValidator(productValidator);
        binder.setAllowedFields(ParameterValues.PRODUCT_NAME, ParameterValues.PRODUCT_PRICE, ParameterValues.PRODUCT_DESCRIPTION,
                ParameterValues.PRODUCT_MANUFACTURER, ParameterValues.PRODUCT_CATEGORY, ParameterValues.PRODUCT_UNITS_IN_STOCK,
                ParameterValues.PRODUCT_MULTIPART_FILE_IMAGE, ParameterValues.PRODUCT_MULTIPART_FILE_MANUAL);
    }

    @GetMapping(PageMappings.DOWNLOAD + "/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id){
        Product product = productService.findById(id);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(product.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + product.getFileName() +"\"")
                .body(new ByteArrayResource(product.getManual()));
    }

    @RequestMapping(PageMappings.INVALID_PROMO_CODE)
    public String invalidPromoCode(){
        return ViewNames.INVALID_PROMO_CODE;
    }
}
