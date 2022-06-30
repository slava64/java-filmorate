package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Genre that = (Genre) o;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = id == null ? 0 : id.hashCode();
        result = 31 * result;
        result = 31 * result;
        return result;
    }
}
