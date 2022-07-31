package com.example.springjwtauthentication.service;

import com.example.springjwtauthentication.exceptions.UserAlreadyExistsException;
import com.example.springjwtauthentication.model.entity.Role;
import com.example.springjwtauthentication.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final JwtService jwtService;

    public User registerClient(User user) {
        log.debug("Registering client: {}", user.getEmail());

        Optional<User> possibleUser = userService.getUser(user.getEmail());
        if (possibleUser.isPresent()) {
            log.error("User with email: {} already exist", user.getEmail());

            throw new UserAlreadyExistsException(user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CLIENT);

        return userService.saveUser(user);
    }

    public String authenticateUser(String email, String password) {
        log.debug("Authenticate user: {}", email);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        authentication = authenticationManager.authenticate(authentication);

        return jwtService.buildToken(authentication);
    }
}
