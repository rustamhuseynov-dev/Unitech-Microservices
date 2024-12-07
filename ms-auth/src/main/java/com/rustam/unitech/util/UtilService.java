package com.rustam.unitech.util;

import com.rustam.unitech.config.PasswordEncoderConfig;
import com.rustam.unitech.dto.request.ResetPasswordRequest;
import com.rustam.unitech.exception.custom.IncorrectPasswordException;
import com.rustam.unitech.exception.custom.InvalidUUIDFormatException;
import com.rustam.unitech.exception.custom.UserNotFoundException;
import com.rustam.unitech.model.User;
import com.rustam.unitech.repository.UserRepository;
import com.rustam.unitech.service.UserService;
import com.rustam.unitech.service.user.UserDetailsServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Slf4j
public class UtilService {

    UserRepository userRepository;
    PasswordEncoderConfig passwordEncoderConfig;

    public UUID convertToUUID(String id) {
        try {
            log.info("id {}",id);
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDFormatException("Invalid UUID format for ID: " + id, e);
        }
    }

    public User findByUsername(String username) {
       return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User Not Found with username: " + username));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No such user found."));
    }

    public void utilResetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No such user found."));
        if (resetPasswordRequest.getPassword().equals(resetPasswordRequest.getNewPassword())){
            user.setPassword(passwordEncoderConfig.passwordEncoder().encode(resetPasswordRequest.getPassword()));
            userRepository.save(user);
        }else {
            throw new IncorrectPasswordException("password does not match");
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No such user found."));
    }
}
