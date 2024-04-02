package com.qnit18.springadvanced.repository;

import com.qnit18.springadvanced.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
