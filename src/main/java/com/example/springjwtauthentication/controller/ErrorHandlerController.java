package com.example.springjwtauthentication.controller;

import com.example.springjwtauthentication.exceptions.UserAlreadyExistsException;
import com.example.springjwtauthentication.model.error.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(BadCredentialsException e) {
        log.error("handleAuthenticationException: {}", e.getMessage(), e);

        String error = "Invalid username or password";
        return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, error, e));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDenied: {}", e.getMessage(), e);

        String error = "JWT token cannot be trusted";
        return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, error, e));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error("handleHttpMessageNotReadable: {}", e.getMessage(), e);

        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, e));
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ApiError> handleIllegalStateException(IllegalStateException e) {
        log.error("handleIllegalStateException: {}", e.getMessage(), e);

        return buildResponseEntity(new ApiError(BAD_REQUEST, e.getMessage(), e));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<ApiError> handleUserAlreadyExists(UserAlreadyExistsException e) {
        log.error("handleUserAlreadyExistsException: {}", e.getMessage(), e);

        return buildResponseEntity(new ApiError(BAD_REQUEST, e.getMessage(), e));
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentNotValid(BindException e) {
        log.error("handleMessageArgNotValid: {}", e.getMessage(), e);

        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.setDebugMessage(e.getMessage());
        apiError.addValidationErrors(e.getBindingResult().getFieldErrors());

        return buildResponseEntity(apiError);
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
