package com.example.springjwtauthentication.controller;

import com.example.springjwtauthentication.model.entity.User;
import com.example.springjwtauthentication.model.dto.UserDto;
import com.example.springjwtauthentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(){
        List<User> users = userService.getAll();

        return users.stream().map(this::mapUserToUserDto).collect(Collectors.toList());
    }

    @GetMapping("current")
    public UserDto userDto(Authentication auth) {
        String email = auth.getName();

        User user = userService.getUser(email).orElseThrow(() -> new IllegalStateException("User wasn't found"));

        return mapUserToUserDto(user);
    }

    private UserDto mapUserToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
