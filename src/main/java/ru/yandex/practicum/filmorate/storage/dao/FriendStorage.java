package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

public interface FriendStorage {
    // Добавить в друзья
    public void add(User user, User friend);
    // Удалить из друзей
    public Boolean delete(User user, User friend);
}
