package com.example.taskmanagementsystem.user.service.impl;

import com.example.taskmanagementsystem.exception.BadRequestException;
import com.example.taskmanagementsystem.exception.ErrorResponse;
import com.example.taskmanagementsystem.exception.NotFoundException;
import com.example.taskmanagementsystem.exception.UnauthorizedException;
import com.example.taskmanagementsystem.user.dto.IncomeUserDto;
import com.example.taskmanagementsystem.user.dto.JwtRequest;
import com.example.taskmanagementsystem.user.dto.JwtResponse;
import com.example.taskmanagementsystem.user.dto.UserDto;
import com.example.taskmanagementsystem.user.model.User;
import com.example.taskmanagementsystem.user.service.AuthService;
import com.example.taskmanagementsystem.user.service.UserService;
import com.example.taskmanagementsystem.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        User user = userService.findByUserByEmail(authRequest.getEmail());
        String password = user.getPassword();
        if (!(passwordEncoder.matches(authRequest.getPassword(), password))) {
            throw new UnauthorizedException("Неверный логин или пароль");
        }
        UserDetails userDetails = userService.loadUserByEmail(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Override
    public ResponseEntity<?> createNewUser(@RequestBody IncomeUserDto incomeUserDto) {
        UserDto user = userService.addUser(incomeUserDto);
        return ResponseEntity.ok(user);
    }

}