package com.example.taskmanagementsystem.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorResponse {

    private String status;

    private String reason;

    private String message;

    private LocalDateTime timestamp;
}
