package com.qnit18.springadvanced.mapper;

import com.qnit18.springadvanced.dto.request.UserCreationRequest;
import com.qnit18.springadvanced.dto.request.UserUpdateRequest;
import com.qnit18.springadvanced.dto.response.UserResponse;
import com.qnit18.springadvanced.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
