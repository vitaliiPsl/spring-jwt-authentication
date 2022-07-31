package com.example.springjwtauthentication.model.dto;

import com.example.springjwtauthentication.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    private String fullName;

    @Email(message = "Email address has to be valid")
    @NotBlank(message = "Email address is required")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Role role;
}
