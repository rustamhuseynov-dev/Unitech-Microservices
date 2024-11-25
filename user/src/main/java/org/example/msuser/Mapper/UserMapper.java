package org.example.msuser.Mapper;

import org.example.msuser.DTO.UserRequest;
import org.example.msuser.DTO.UserResponse;
import org.example.msuser.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring"
        , unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse mapToDto(User user);

    Set<UserResponse> mapToDto(Set<User> users);

    User mapToEntity(UserRequest userRequest);
}


