package com.rustam.unitech.controller;

import com.rustam.unitech.dto.request.*;
import com.rustam.unitech.dto.response.AuthResponse;
import com.rustam.unitech.dto.response.UserResponse;
import com.rustam.unitech.service.AuthService;
import com.rustam.unitech.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AuthController {

    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){
        return new ResponseEntity<>(authService.login(authRequest), HttpStatus.OK);
    }

    @PostMapping(path = "/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody RefreshRequest refreshRequest){
        return new ResponseEntity<>(authService.refreshToken(refreshRequest),HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public String logout(@RequestBody RefreshRequest refreshRequest){
        return authService.logout(refreshRequest);
    }

    @PutMapping(path = "/forgot-your-password")
    public String forgotYourPassword(@RequestBody ForgotYourPasswordRequest forgotYourPasswordRequest){
        return authService.forgotYourPassword(forgotYourPasswordRequest);
    }

    @PutMapping(path = "/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        return authService.resetPassword(resetPasswordRequest);
    }
}
