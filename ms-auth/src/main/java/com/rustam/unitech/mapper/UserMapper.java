package com.rustam.unitech.mapper;

import com.rustam.unitech.dto.request.UserUpdateRequest;
import com.rustam.unitech.dto.response.UserResponse;
import com.rustam.unitech.model.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserMapper {

    List<UserResponse> toResponses(List<User> users);

    UserResponse toResponse(User user);
}
