package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface LikeStorage {
    // Добавляет лайк
    public void add(Film film, User user);
    // Удаляет лайк
    public Boolean delete(Film film, User user);
}
