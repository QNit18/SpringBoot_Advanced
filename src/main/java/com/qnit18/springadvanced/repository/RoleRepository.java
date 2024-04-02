package com.qnit18.springadvanced.repository;

import com.qnit18.springadvanced.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {
}
