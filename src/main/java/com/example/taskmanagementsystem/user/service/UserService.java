package com.example.taskmanagementsystem.user.service;

import com.example.taskmanagementsystem.user.dto.IncomeUserDto;
import com.example.taskmanagementsystem.user.dto.UserDto;
import com.example.taskmanagementsystem.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDto addUser(IncomeUserDto incomeUserDto);

    User findByUserByEmail(String email);

    UserDetails loadUserByEmail(String email);

}