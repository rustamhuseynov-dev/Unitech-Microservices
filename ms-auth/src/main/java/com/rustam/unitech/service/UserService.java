package com.rustam.unitech.service;

import com.rustam.unitech.dto.request.ResetPasswordRequest;
import com.rustam.unitech.dto.request.UserRequest;
import com.rustam.unitech.dto.request.UserUpdateRequest;
import com.rustam.unitech.dto.response.UserDeletedResponse;
import com.rustam.unitech.dto.response.UserResponse;
import com.rustam.unitech.exception.custom.ExistsException;
import com.rustam.unitech.exception.custom.IncorrectPasswordException;
import com.rustam.unitech.exception.custom.UserNotFoundException;
import com.rustam.unitech.mapper.UserMapper;
import com.rustam.unitech.model.User;
import com.rustam.unitech.model.enums.Role;
import com.rustam.unitech.repository.UserRepository;
import com.rustam.unitech.service.kafka.KafkaProducerService;
import com.rustam.unitech.service.user.UserDetailsServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;
    UserMapper userMapper;
    KafkaProducerService kafkaProducerService;

    public UserResponse save(UserRequest userRequest) {
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .authorities(Collections.singleton(Role.ROLE_USER))
                .build();
        userRepository.save(user);
        return UserResponse.builder()
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .build();
    }

    public UserResponse update(UserUpdateRequest userUpdateRequest) {
        User user = findById(userUpdateRequest.getId());
        boolean exists = findAll().stream()
                .map(User::getUsername)
                .anyMatch(existingUsername -> existingUsername.equals(userUpdateRequest.getUsername()));
        if (exists){
            throw new ExistsException("This username is already taken.");
        }
        modelMapper.map(userUpdateRequest,user);
        user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        userRepository.save(user);
        UserResponse response = UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .build();
        kafkaProducerService.sendMessage(response);
        return response;
    }

    public List<UserResponse> readAll() {
        List<User> users = findAll();
        return userMapper.toResponses(users);
    }

    private List<User> findAll() {
        return userRepository.findAll();
    }

    public UserResponse read(UUID id) {
        User user = findById(id);
        return userMapper.toResponse(user);
    }

    private User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No such user found."));
    }

    public UserDeletedResponse remove(UUID id) {
        User user = findById(id);
        UserDeletedResponse deletedResponse = new UserDeletedResponse();
        modelMapper.map(user,deletedResponse);
        deletedResponse.setText("This user was deleted by you.");
        userRepository.delete(user);
        return deletedResponse;
    }
}
