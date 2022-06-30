package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Director {
    Long id;

    @NotNull
    @NotBlank
    String name;

    public Director(String name) {
        this.name = name;
    }

    public Director(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
