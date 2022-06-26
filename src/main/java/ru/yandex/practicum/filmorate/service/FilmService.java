package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll().values();
    }

    public List<Film> findAllPopular(Integer count) {
        Collection<Film> films = filmStorage.findAll().values();
        return films.stream()
                .sorted((p0, p1) -> Integer.compare(p1.getLikes().size(), p0.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findOne(Long id) {
        return filmStorage.findOne(id).orElseThrow(
                () -> new FilmNotFoundException(String.format("Фильм %d не найден", id)));
    }

    public Film create(Film film) {
        checkReleaseDate(film.getReleaseDate());
        filmStorage.add(film);
        return film;
    }

    public Film update(Film film) {
        findOne(film.getId());
        checkReleaseDate(film.getReleaseDate());
        filmStorage.update(film);
        return film;
    }

    public Film delete(Long id) {
        Film film = findOne(id);
        filmStorage.delete(id);
        return film;
    }

    public Film addLike(Long id, Long userId) {
        User user = userService.findOne(userId);
        Film film = findOne(id);
        filmStorage.addLike(film, user);
        return film;
    }

    public Film deleteLike(Long id, Long userId) {
        User user = userService.findOne(userId);
        Film film = findOne(id);
        filmStorage.deleteLike(film, user);
        return film;
    }

    private void checkReleaseDate(LocalDate date) {
        LocalDate minDate = LocalDate.of(1895, Month.DECEMBER, 28);
        if (minDate.isAfter(date)) {
            throw new ReleaseDateException("Дата релиза должна быть больше 28 декабря 1895 года");
        }
    }
}
