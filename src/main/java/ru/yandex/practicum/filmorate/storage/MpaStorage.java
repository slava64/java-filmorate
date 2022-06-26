package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Map;
import java.util.Optional;

public interface MpaStorage {
    // Возвращает все жанры
    public Map<Long, Mpa> findAll();
    // Возвращает один жанр
    public Optional<Mpa> findOne(Long id);
}