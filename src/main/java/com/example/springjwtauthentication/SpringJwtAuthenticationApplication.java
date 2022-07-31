package com.example.springjwtauthentication;

import com.example.springjwtauthentication.model.entity.Role;
import com.example.springjwtauthentication.model.entity.User;
import com.example.springjwtauthentication.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class SpringJwtAuthenticationApplication {
    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    public static void main(String[] args) {
        SpringApplication.run(SpringJwtAuthenticationApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserService userService, PasswordEncoder encoder) {
        return args -> {
            User admin = getAdmin(encoder);

            Optional<User> possibleUser = userService.getUser(admin.getEmail());
            if (possibleUser.isEmpty()) {
                userService.saveUser(admin);
            }
        };
    }

    private User getAdmin(PasswordEncoder encoder) {
        return User.builder()
                .email(adminEmail)
                .password(encoder.encode(adminPassword))
                .role(Role.ADMIN)
                .build();
    }
}
