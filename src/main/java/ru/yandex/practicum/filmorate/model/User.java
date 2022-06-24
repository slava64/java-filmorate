package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;

    @NotNull @NotBlank @Email
    private String email;

    @NotNull @NotBlank @Pattern(regexp = "^\\S*$")
    private String login;

    private String name;

    @Past @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();
}
