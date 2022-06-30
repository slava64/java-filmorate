package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/directors")
@Slf4j
@Validated
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public Collection<Director> findAll() {
        return directorService.findAll();
    }


    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable("id") Long id) {
        return directorService.getDirectorById(id);
    }

    @PostMapping
    public Director create(@Valid @RequestBody Director director, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Тело: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), director.toString());
        return directorService.create(director);
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Тело: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), director.toString());
        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return "Режиссер " + directorService.delete(id) + " удален.";
    }

}
