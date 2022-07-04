package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/common")
    public Collection<Film> findCommon(
            @RequestParam Long userId,
            @RequestParam Long friendId) {
        return filmService.findCommon(userId, friendId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count,
                                            @RequestParam(required = false) Integer year,
                                            @RequestParam(required = false) Integer genreId) {
        log.info("Get {} popular films", count);
        return filmService.getPopularFilms(count, genreId, year);
    }

    @GetMapping("/{id}")
    public Film findOne(@PathVariable("id") Long id) {
        return filmService.findOne(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Тело: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), film.toString());
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Тело: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), film.toString());
        return filmService.update(film);
    }

    @DeleteMapping("/{id}")
    public Film delete(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmService.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(
            @PathVariable("id") Long id,
            @PathVariable("userId") Long userId,
            HttpServletRequest request
    ) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(
            @PathVariable("id") Long id,
            @PathVariable("userId") Long userId,
            HttpServletRequest request
    ) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/director/{directorId}")
    public Collection<Film> getSortedFilmsByDirectorId(
            @PathVariable Long directorId,
            @RequestParam Optional<String> sortBy
    ){
        return filmService.getSortedFilmsByDirectorId(directorId, sortBy);

    }
}
