package az.edu.turing.msauth1.service;

import az.edu.turing.msauth1.dao.entity.UserEntity;
import az.edu.turing.msauth1.dao.repository.UserRepository;
import az.edu.turing.msauth1.dto.request.LoginRequest;
import az.edu.turing.msauth1.dto.request.RegisterRequest;
import az.edu.turing.msauth1.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String registerUser(RegisterRequest registerRequest) {
//        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
//            throw new RuntimeException("Username is already in use");
//        }
        UserEntity user = new UserEntity();
        user.setFullName(registerRequest.getFullname());
        user.setRole(registerRequest.getRole());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
        return "User registered successfully";
    }

    public String loginUser(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials.");
        }
        return jwtUtil.generateToken(user.getUsername());
    }
}


