package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long id = Long.valueOf(1);

    @Override
    public void add(User user) {
        if (user.getId() == null) {
            user.setId(id++);
        }
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public Map<Long, User> findAll() {
        return users;
    }

    @Override
    public User findOne(Long id) {
        return users.get(id);
    }

    @Override
    public User addFriend(Long id, Long friendId) {
        User user = users.get(id);
        user.getFriends().add(friendId);
        User friend = users.get(friendId);
        friend.getFriends().add(id);
        return user;
    }

    @Override
    public User deleteFriend(Long id, Long friendId) {
        User user = users.get(id);
        user.getFriends().remove(friendId);
        User friend = users.get(friendId);
        friend.getFriends().remove(id);
        return user;
    }
}
