package com.example.taskmanagementsystem.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCommentDto {

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(min = 5, max = 200)
    private String text;

}