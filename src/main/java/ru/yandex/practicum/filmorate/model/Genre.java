package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Genre implements Comparable<Genre> {
    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Long id;

    @NotNull @NotBlank @Size(max=50)
    private String name;

    @Override
    public int compareTo(Genre o) {
        return (int) (this.getId() - o.getId());
    }
}
