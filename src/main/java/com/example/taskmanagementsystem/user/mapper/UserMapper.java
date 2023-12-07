package com.example.taskmanagementsystem.user.mapper;

import com.example.taskmanagementsystem.user.dto.IncomeUserDto;
import com.example.taskmanagementsystem.user.dto.ShortUserDto;
import com.example.taskmanagementsystem.user.dto.UserDto;
import com.example.taskmanagementsystem.user.model.User;
import com.example.taskmanagementsystem.user.model.enums.Roles;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.List;

@UtilityClass
public class UserMapper {

    public User toUser(IncomeUserDto incomeUserDto) {
        return User.builder()
                .name(incomeUserDto.getName())
                .email(incomeUserDto.getEmail())
                .password(incomeUserDto.getPassword())
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public ShortUserDto toShortUserDto(User user) {
        return ShortUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
