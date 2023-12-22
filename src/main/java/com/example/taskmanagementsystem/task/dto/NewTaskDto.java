package com.example.taskmanagementsystem.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewTaskDto {

    @NotNull(message = "Ошибка валидации. Id создателя не может быть пустым")
    private long creator_id;

    @NotBlank(message = "Ошибка валидации. Заголовок не может быть пустым")
    @Size(min = 3, max = 155, message = "Ошибка валидации. Размер заголовка должен быть от 3 до 155 знаков")
    private String title;

    @NotBlank(message = "Ошибка валидации. Описание не может быть пустым")
    @Size(min = 20, max = 1000, message = "Ошибка валидации. Размер описания должен быть от 20 до 1000 знаков")
    private String description;

    @NotBlank(message = "Ошибка валидации. Статус не может быть пустым")
    private String status;

    @NotBlank(message = "Ошибка валидации. приоритет не может быть пустым")
    private String priority;

    private Long executor_id;

}