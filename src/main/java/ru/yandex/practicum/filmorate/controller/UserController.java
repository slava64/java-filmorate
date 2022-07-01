package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final UserService userService;
    private final FilmService filmService;
    private final EventService eventService;

    public UserController(UserService userService, FilmService filmService, EventService eventService) {
        this.userService = userService;
        this.filmService = filmService;
        this.eventService = eventService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable("id") Long id) {
        return userService.findOne(id);
    }

    @GetMapping("/{id}/recommendations")
    public Set<Film> findRecommendations(@PathVariable("id") Long id) {
        return filmService.findRecommendations(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable("id") Long id) {
        return userService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findAllFriendsCommon(
            @PathVariable("id") Long id,
            @PathVariable("otherId") Long otherId
    ) {
        return userService.findAllFriendsCommon(id, otherId);
    }

    @GetMapping("/{id}/feed")
    public Collection<Event> findAllEvents(
            @PathVariable("id") Long id
    ) {
        return eventService.findAllByUserId(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Тело: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), user.toString());
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Тело: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), user.toString());
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public User delete(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userService.delete(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(
            @PathVariable("id") Long id,
            @PathVariable("friendId") Long friendId,
            HttpServletRequest request
    ) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(
            @PathVariable("id") Long id,
            @PathVariable("friendId") Long friendId,
            HttpServletRequest request
    ) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userService.deleteFriend(id, friendId);
    }
}
