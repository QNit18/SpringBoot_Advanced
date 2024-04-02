package com.qnit18.springtutorial.mapper;

import com.qnit18.springtutorial.dto.request.UserCreationRequest;
import com.qnit18.springtutorial.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);
}
