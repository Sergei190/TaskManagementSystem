package com.example.taskmanagementsystem.user.controller;

import com.example.taskmanagementsystem.user.dto.IncomeUserDto;
import com.example.taskmanagementsystem.user.dto.JwtRequest;
import com.example.taskmanagementsystem.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createNewUser(@RequestBody @Valid IncomeUserDto incomeUserDto) {
        log.info("Запрос на добавление пользователя");
        return authService.createNewUser(incomeUserDto);
    }
}
