package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.Optional;

public interface UserStorage {
    // Добавляет нового пользователя
    public void add(User user);
    // Добавляет нового друга
    public void addFriend(User user, User friend);
    // Обновляет пользователя
    public void update(User user);
    // Удаляет пользователя
    public Boolean delete(Long id);
    // Удаляет друзей
    public Boolean deleteFriend(User user, User friend);
    // Возвращает всех пользователей
    public Map<Long, User> findAll();
    // Возвращает одного пользователя
    public Optional<User> findOne(Long id);
}
