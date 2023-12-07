package com.example.taskmanagementsystem.user.service;

import com.example.taskmanagementsystem.user.dto.IncomeUserDto;
import com.example.taskmanagementsystem.user.dto.JwtRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {

    ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest);

    ResponseEntity<?> createNewUser(@RequestBody IncomeUserDto incomeUserDto);
}
