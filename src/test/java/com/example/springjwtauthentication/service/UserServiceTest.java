package com.example.springjwtauthentication.service;

import com.example.springjwtauthentication.model.entity.User;
import com.example.springjwtauthentication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void testLoadUserByUsername() {
        // given
        String email = "test@mail.com";
        User user = User.builder()
                .email(email)
                .build();

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(email);

        // then
        verify(userRepository).findByEmail(email);
        assertThat(result.getUsername(), is(email));
    }

    @Test
    void testLoadUserByUsernameThrowsExceptionIfNotFound() {
        // given
        String email = "test@mail.com";

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // then
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }

    @Test
    void saveUser() {
        // given
        User user = User.builder().email("test@mail.com").build();

        // when
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        // then
        verify(userRepository).save(user);
        assertThat(result, is(user));
    }

    @Test
    void testGetUserById() {
        // given
        long id = 2;
        User user = User.builder().id(id).build();

        // when
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(id);

        // then
        verify(userRepository).findById(id);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(user));
    }

    @Test
    void testGetUserByIdReturnsEmptyOptionalIfNotFound() {
        // given
        long id = 2;

        // when
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(id);

        // then
        verify(userRepository).findById(id);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    void testGetUser() {
        // given
        String email = "test@mail.com";
        User user = User.builder().email(email).build();

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(email);

        // then
        verify(userRepository).findByEmail(email);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(user));
    }

    @Test
    void testGetUserByEmailReturnsEmptyOptionalIfNotFound() {
        // given
        String email = "test@mail.com";

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(email);

        // then
        verify(userRepository).findByEmail(email);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    void testGetAll() {
        // given
        List<User> users = List.of(
                User.builder().id(1).build(),
                User.builder().id(2).build()
        );

        // when
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        // then
        verify(userRepository).findAll();
        assertThat(result, hasSize(users.size()));
    }
}