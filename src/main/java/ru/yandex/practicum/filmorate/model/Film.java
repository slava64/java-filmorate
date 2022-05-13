package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private long id;

    @NotNull @NotBlank
    private String name;

    @NotNull @NotBlank @Size(max=200)
    private String description;

    @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate releaseDate;

    @NotNull @Min(1)
    private int duration;
}
