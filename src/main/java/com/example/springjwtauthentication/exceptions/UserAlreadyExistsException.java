package com.example.springjwtauthentication.exceptions;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(long identifier){
        super(String.format("User with identifier: '%d' already exists", identifier));
    }

    public UserAlreadyExistsException(String identifier){
        super(String.format("User with identifier: '%s' already exists", identifier));
    }
}
