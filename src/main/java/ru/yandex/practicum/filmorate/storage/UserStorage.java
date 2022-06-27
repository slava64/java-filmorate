package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.Optional;

public interface UserStorage {
    // Добавляет нового пользователя
    public void add(User user);
    // Обновляет пользователя
    public void update(User user);
    // Удаляет пользователя
    public Boolean delete(Long id);
    // Возвращает всех пользователей
    public Map<Long, User> findAll();
    // Возвращает одного пользователя
    public Optional<User> findOne(Long id);
}
