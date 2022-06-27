package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long id = Long.valueOf(1);

    @Override
    public void add(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Boolean delete(Long id) {
        users.remove(id);
        return true;
    }

    @Override
    public Map<Long, User> findAll() {
        return users;
    }

    @Override
    public Optional<User> findOne(Long id) {
        return Optional.ofNullable(users.get(id));
    }
}
