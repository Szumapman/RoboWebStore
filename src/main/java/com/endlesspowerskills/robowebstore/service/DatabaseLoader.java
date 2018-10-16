package com.endlesspowerskills.robowebstore.service;

import com.endlesspowerskills.robowebstore.repository.AdminRepository;
import com.endlesspowerskills.robowebstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseLoader {

    // -- fields
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;

    // -- constructors
    @Autowired
    public DatabaseLoader(AdminRepository adminRepository, ProductRepository productRepository) {
        this.adminRepository = adminRepository;
        this.productRepository = productRepository;
    }

    // -- init
//    @PostConstruct
//    private void initDatabase(){
//        Admin admin = new Admin();
//        admin.setUsername("Admin3");
//        admin.setPassword("Admin2#$#%fs!@#$#fgsdf#@$fs");
//        adminRepository.save(admin);
//    }
}
