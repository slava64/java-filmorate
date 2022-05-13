package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Тело: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), film.toString());
        checkReleaseDate(film.getReleaseDate());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Тело: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), film.toString());
        checkReleaseDate(film.getReleaseDate());
        films.put(film.getId(), film);
        return film;
    }

    private void checkReleaseDate(LocalDate date) {
        LocalDate minDate = LocalDate.of(1895, Month.DECEMBER, 28);
        if (minDate.isAfter(date)) {
            log.warn("Дата релиза должна быть больше 28 декабря 1895 года");
            throw new ReleaseDateException("Дата релиза должна быть больше 28 декабря 1895 года");
        }
    }
}
