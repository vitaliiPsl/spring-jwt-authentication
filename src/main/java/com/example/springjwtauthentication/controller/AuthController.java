package com.example.springjwtauthentication.controller;

import com.example.springjwtauthentication.model.entity.User;
import com.example.springjwtauthentication.model.dto.AuthenticationRequestDto;
import com.example.springjwtauthentication.model.dto.UserDto;
import com.example.springjwtauthentication.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final ModelMapper modelMapper;
    private final AuthService authService;

    @PostMapping("/signup")
    public UserDto signup(@Valid @RequestBody UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);

        user = authService.registerClient(user);

        return modelMapper.map(user, UserDto.class);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthenticationRequestDto authRequest) {
        String jwt = authService.authenticateUser(authRequest.getEmail(), authRequest.getPassword());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }
}
