package com.endlesspowerskills.robowebstore.repository;

import com.endlesspowerskills.robowebstore.entity.Admin;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, String> {
    Admin getAdminByUsername(String username);
}
