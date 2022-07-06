package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Map;
import java.util.Optional;

public interface GenreStorage {
    // Возвращает все жанры
    public Map<Long, Genre> findAll();
    // Возвращает один жанр
    public Optional<Genre> findOne(Long id);
}
