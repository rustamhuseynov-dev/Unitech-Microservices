package com.rustam.unitech.controller;

import com.rustam.unitech.dto.request.UserRequest;
import com.rustam.unitech.dto.request.UserUpdateRequest;
import com.rustam.unitech.dto.response.UserResponse;
import com.rustam.unitech.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UserController {

    UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<UserResponse> save(@RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userService.save(userRequest), HttpStatus.CREATED);
    }

}
