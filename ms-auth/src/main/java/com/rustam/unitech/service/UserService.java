package com.rustam.unitech.service;

import com.rustam.unitech.dto.request.UserRequest;
import com.rustam.unitech.dto.request.UserUpdateRequest;
import com.rustam.unitech.dto.response.UserResponse;
import com.rustam.unitech.exception.custom.UserNotFoundException;
import com.rustam.unitech.mapper.UserMapper;
import com.rustam.unitech.model.User;
import com.rustam.unitech.model.enums.Role;
import com.rustam.unitech.repository.UserRepository;
import com.rustam.unitech.service.kafka.KafkaProducerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    KafkaProducerService kafkaProducerService;

    public UserResponse save(UserRequest userRequest) {
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .name(userRequest.getName())
                .authorities(Collections.singleton(Role.ROLE_USER))
                .build();
        userRepository.save(user);
        return UserResponse.builder()
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .build();
    }

    public UserResponse update(UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userUpdateRequest.getId())
                .orElseThrow(() -> new UserNotFoundException("No such user found."));
        UserResponse response = userMapper.toDto(user);
        userRepository.save(user);
        kafkaProducerService.sendMessage(response);
        return response;
    }
}
