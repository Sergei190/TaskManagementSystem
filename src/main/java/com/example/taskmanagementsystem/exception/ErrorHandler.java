package com.example.taskmanagementsystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        log.info("400: {}", e.getMessage());
        String reason = "BAD_REQUEST";
        return getErrorResponse(HttpStatus.BAD_REQUEST, reason, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.info("404: {}", e.getMessage());
        String reason = "The required object was not found.";
        return getErrorResponse(HttpStatus.NOT_FOUND, reason, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) //409
    public ErrorResponse handleConflictException(final ConflictException e) {
        log.info("409: {}", e.getMessage());
        String reason = "CONFLICT";
        return getErrorResponse(HttpStatus.CONFLICT, reason, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED) //401
    public ErrorResponse handleUnauthorizedException(final UnauthorizedException e) {
        log.info("401: {}", e.getMessage());
        String reason = "Ошибка авторизации";
        return getErrorResponse(HttpStatus.UNAUTHORIZED, reason, e.getMessage());
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(Exception ex) {
        log.info("401: {}", ex.getMessage());
        String reason = "Ошибка аутентификации";
        return getErrorResponse(HttpStatus.UNAUTHORIZED, reason, ex.getMessage());
    }

    private ErrorResponse getErrorResponse(HttpStatus status, String reason, String message) {
        return ErrorResponse.builder()
                .status(status.name())
                .reason(reason)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
