package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    // Добавляет новый фильм
    public void add(Film film);
    // Обновляет фильм
    public void update(Film film);
    // Удаляет фильм
    public void delete(Film film);
    // Возвращает все фильмы
    public Map<Long, Film> findAll();
    // Возвращает один фильм
    public Film findOne(Long id);
    // Добавляет лайк
    public Film addLike(Long id, Long userId);
    // Удаляет лайк
    public Film deleteLike(Long id, Long userId);
}
