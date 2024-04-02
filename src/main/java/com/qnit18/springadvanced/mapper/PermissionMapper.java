package com.qnit18.springadvanced.mapper;

import com.qnit18.springadvanced.dto.request.PermissionRequest;
import com.qnit18.springadvanced.dto.request.UserCreationRequest;
import com.qnit18.springadvanced.dto.request.UserUpdateRequest;
import com.qnit18.springadvanced.dto.response.PermissionResponse;
import com.qnit18.springadvanced.dto.response.UserResponse;
import com.qnit18.springadvanced.entity.Permission;
import com.qnit18.springadvanced.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

}
