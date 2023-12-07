package com.example.taskmanagementsystem.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class IncomeUserDto {

    @NotBlank(message = "Ошибка валидации. Имя не может быть пустым")
    @Size(min = 1, message = "Ошибка валидации. Имя не может быть короче 1 буквы")
    private String name;

    @Email(message = "Ошибка валидации. Не верный формат электронной почты")
    @NotBlank(message = "Ошибка валидации. Электронная почта не может быть пустой")
    private String email;

    @NotBlank(message = "Ошибка валидации. Пароль не может быть пустой")
    @Size(min = 6, message = "Ошибка валидации. Пароль не может быть короче 6 символов")
    private String password;
}
