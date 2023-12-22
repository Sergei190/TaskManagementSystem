package com.example.taskmanagementsystem.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class JwtRequest {

    private String email;
    private String password;

}