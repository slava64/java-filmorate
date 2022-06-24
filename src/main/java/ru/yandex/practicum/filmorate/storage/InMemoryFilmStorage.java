package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    public final Map<Long, Film> films = new HashMap<>();
    private Long id = Long.valueOf(1);

    @Override
    public void add(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public Film delete(Long id) {
        return films.remove(id);
    }

    @Override
    public Map<Long, Film> findAll() {
        return films;
    }

    @Override
    public Optional<Film> findOne(Long id) {
        return Optional.ofNullable(films.get(id));
    }
}
