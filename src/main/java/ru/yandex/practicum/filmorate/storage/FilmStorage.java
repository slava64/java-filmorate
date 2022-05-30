package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;
import java.util.Optional;

public interface FilmStorage {
    // Добавляет новый фильм
    public void add(Film film);
    // Обновляет фильм
    public void update(Film film);
    // Удаляет фильм
    public Film delete(Long id);
    // Возвращает все фильмы
    public Map<Long, Film> findAll();
    // Возвращает один фильм
    public Optional<Film> findOne(Long id);
}
