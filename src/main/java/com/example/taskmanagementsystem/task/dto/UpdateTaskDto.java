package com.example.taskmanagementsystem.task.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDto {

    @Size(min = 3, max = 155, message = "Ошибка валидации. Размер заголовка должен быть от 3 до 155 знаков")
    private String title;

    @Size(min = 20, max = 1000, message = "Ошибка валидации. Размер описания должен быть от 20 до 1000 знаков")
    private String description;

    private String status;

    private String priority;

    private Long executor_id;

}