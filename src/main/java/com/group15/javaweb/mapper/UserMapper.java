package com.group15.javaweb.mapper;

import com.group15.javaweb.dto.UserCreationRequest;
import com.group15.javaweb.dto.UpdateMeRequest;
import com.group15.javaweb.dto.request.RegisterRequest;
import com.group15.javaweb.dto.response.client.user.UserResponse;
import com.group15.javaweb.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User toUser(RegisterRequest request);
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UpdateMeRequest request);
}
