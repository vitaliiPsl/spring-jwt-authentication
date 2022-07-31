package com.example.springjwtauthentication.service;

import com.example.springjwtauthentication.model.entity.User;
import com.example.springjwtauthentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            log.error("User wasn't found");
            throw new UsernameNotFoundException(String.format("User with email: %s wasn't found", username));
        }

        return user.get();
    }

    public User saveUser(User user) {
        log.debug("Saving user: {}", user);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUser(long id) {
        log.debug("Fetching user by id: {}", id);

        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUser(String email) {
        log.debug("Fetching user by email: {}", email);

        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        log.debug("Fetching all users");

        return userRepository.findAll();
    }
}
