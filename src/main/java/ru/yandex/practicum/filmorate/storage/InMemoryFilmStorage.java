package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    public final Map<Long, Film> films = new HashMap<>();
    private Long id = Long.valueOf(1);

    @Override
    public void add(Film film) {
        if (film.getId() == null) {
            film.setId(id++);
        }
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public void delete(Film film) {
        films.remove(film.getId());
    }

    @Override
    public Map<Long, Film> findAll() {
        return films;
    }

    @Override
    public Film findOne(Long id) {
        return films.get(id);
    }

    @Override
    public Film addLike(Long id, Long userId) {
        Film film = films.get(id);
        film.getLikes().add(userId);
        return film;
    }

    @Override
    public Film deleteLike(Long id, Long userId) {
        Film film = films.get(id);
        film.getLikes().remove(userId);
        return film;
    }
}
