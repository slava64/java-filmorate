package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

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
    public void addLike(Film film, User user) {
        if (film.getLikes() != null) {
            film.getLikes().add(user.getId());
        } else {
            Set<Long> likes = new HashSet<>();
            likes.add(user.getId());
            film.setLikes(likes);
        }
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public Boolean delete(Long id) {
        films.remove(id);
        return true;
    }

    @Override
    public Boolean deleteLike(Film film, User user) {
        if (film.getLikes() != null) {
            film.getLikes().remove(user.getId());
        }
        return true;
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
