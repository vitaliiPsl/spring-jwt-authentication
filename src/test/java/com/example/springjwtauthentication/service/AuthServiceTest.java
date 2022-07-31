package com.example.springjwtauthentication.service;

import com.example.springjwtauthentication.exceptions.UserAlreadyExistsException;
import com.example.springjwtauthentication.model.entity.Role;
import com.example.springjwtauthentication.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserService userService;

    @Mock
    JwtService jwtService;

    @InjectMocks
    AuthService authService;

    @Test
    void testRegisterClient() {
        // given
        String email = "test@mail.com";
        String password = "password";
        String encodedPassword = "kj1oh2";

        User user = User.builder()
                .email(email)
                .password(password)
                .build();

        // when
        when(userService.getUser(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userService.saveUser(user)).thenReturn(user);

        // then
        User result = authService.registerClient(user);
        verify(userService).getUser(email);
        verify(passwordEncoder).encode(password);
        verify(userService).saveUser(user);

        assertThat(result.getEmail(), is(email));
        assertThat(result.getPassword(), is(encodedPassword));
        assertThat(result.getRole(), is(Role.CLIENT));
    }


    @Test
    void testRegisterClientThrowsExceptionIfUserAlreadyExists() {
        // given
        String email = "test@mail.com";
        String password = "password";

        User user = User.builder()
                .email(email)
                .password(password)
                .build();

        // when
        when(userService.getUser(email)).thenReturn(Optional.of(user));

        // then
        assertThrows(UserAlreadyExistsException.class, () -> authService.registerClient(user));
    }

    @Test
    void testAuthenticateUser() {
        // given
        String email = "test@mail.com";
        String password = "password";
        String jwt = "jwt";
        Authentication auth = new UsernamePasswordAuthenticationToken(email, password);

        // when
        when(authenticationManager.authenticate(auth)).thenReturn(auth);
        when(jwtService.buildToken(auth)).thenReturn(jwt);

        String result = authService.authenticateUser(email, password);

        // then
        verify(authenticationManager).authenticate(auth);
        verify(jwtService).buildToken(auth);
        assertThat(result, is(jwt));
    }
}