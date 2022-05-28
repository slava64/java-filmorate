package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReleaseDateException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        checkFilm(id);
        return filmStorage.findOne(id);
    }

    public Film create(Film film) {
        checkReleaseDate(film.getReleaseDate());
        filmStorage.add(film);
        return film;
    }

    public Film update(Film film) {
        checkFilm(film.getId());
        checkReleaseDate(film.getReleaseDate());
        filmStorage.update(film);
        return film;
    }

    public Film delete(Film film) {
        checkFilm(film.getId());
        filmStorage.delete(film);
        return film;
    }

    public Film addLike(Long id, Long userId) {
        checkFilm(id);
        checkUser(userId);
        return filmStorage.addLike(id, userId);
    }

    public Film deleteLike(Long id, Long userId) {
        checkFilm(id);
        checkUser(userId);
        return filmStorage.deleteLike(id, userId);
    }

    private void checkReleaseDate(LocalDate date) {
        LocalDate minDate = LocalDate.of(1895, Month.DECEMBER, 28);
        if (minDate.isAfter(date)) {
            throw new ReleaseDateException("Дата релиза должна быть больше 28 декабря 1895 года");
        }
    }

    private void checkFilm(Long id) {
        if (filmStorage.findOne(id) == null) {
            throw new FilmNotFoundException(String.format("Фильм %d не найден", id));
        }
    }

    private void checkUser(Long id) {
        if (userStorage.findOne(id) == null) {
            throw new UserNotFoundException(String.format("Пользователь %d не найден", id));
        }
    }
}
