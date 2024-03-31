package com.qnit18.springadvanced.mapper;

import com.qnit18.springadvanced.dto.request.PermissionRequest;
import com.qnit18.springadvanced.dto.request.RoleRequest;
import com.qnit18.springadvanced.dto.response.PermissionResponse;
import com.qnit18.springadvanced.dto.response.RoleResponse;
import com.qnit18.springadvanced.entity.Permission;
import com.qnit18.springadvanced.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

}
