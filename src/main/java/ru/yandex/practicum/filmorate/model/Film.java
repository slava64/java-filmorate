package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }

    private Long id;

    @NotNull @NotBlank
    private String name;

    @NotNull @NotBlank @Size(max=200)
    private String description;

    @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate releaseDate;

    @NotNull @Min(1)
    private int duration;

    private int rate;

    private Set<Long> likes = new HashSet<>();

    @NotNull
    private Mpa mpa;

    private Set<Genre> genres;

    private List<Director> directors;
}
