package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private long id;

    @NotNull @NotBlank @Email
    private String email;

    @NotNull @NotBlank
    private String login;

    private String name;

    @Past @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;
}
