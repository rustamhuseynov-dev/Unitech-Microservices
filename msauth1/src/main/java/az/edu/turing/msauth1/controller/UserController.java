package az.edu.turing.msauth1.controller;

import az.edu.turing.msauth1.dao.entity.UserEntity;
import az.edu.turing.msauth1.security.JwtUtil;
import az.edu.turing.msauth1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/details")
    public ResponseEntity<String> getUserDetails(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        String username = jwtUtil.extractUsername(token);

        UserEntity userDetails = userService.findByUsername(username);

        if (userDetails != null) {
            return ResponseEntity.ok(String.valueOf(userDetails));
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}
