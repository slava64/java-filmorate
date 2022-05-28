package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    // Добавляет нового пользователя
    public void add(User user);
    // Обновляет пользователя
    public void update(User user);
    // Удаляет пользователя
    public void delete(User user);
    // Возвращает всех пользователей
    public Map<Long, User> findAll();
    // Возвращает одного пользователя
    public User findOne(Long id);
    // Добавляет друга
    public User addFriend(Long id, Long friendId);
    // Удаляет друга
    public User deleteFriend(Long id, Long friendId);
}
