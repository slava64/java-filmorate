package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll().values();
    }

    public User findOne(Long id) {
        return userStorage.findOne(id).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь %d не найден", id)));
    }

    public List<User> findAllFriends(Long id) {
        User user = findOne(id);
        Collection<User> users = userStorage.findAll().values();
        return users
                .stream()
                .filter(x -> user.getFriends().contains(x.getId()))
                .collect(Collectors.toList());
    }

    public List<User> findAllFriendsCommon(Long id, Long friendId) {
        User user = findOne(id);
        User friend = findOne(friendId);
        Set<Long> friendsCommon = getSetFriendsCommon(user.getFriends(), friend.getFriends());
        Collection<User> users = userStorage.findAll().values();
        return users
                .stream()
                .filter(x -> friendsCommon.contains(x.getId()))
                .collect(Collectors.toList());
    }

    public User create(User user) {
        validateUser(user);
        userStorage.add(user);
        return user;
    }

    public User update(User user) {
        validateUser(user);
        findOne(user.getId());
        userStorage.update(user);
        return user;
    }

    public User delete(Long id) {
        findOne(id);
        return userStorage.delete(id);
    }

    public User addFriend(Long id, Long friendId) {
        User user = findOne(id);
        User friend = findOne(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        return user;
    }

    public User deleteFriend(Long id, Long friendId) {
        User user = findOne(id);
        User friend = findOne(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        return user;
    }

    private void validateUser(User user) {
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private Set<Long> getSetFriendsCommon(Set<Long> friends1, Set<Long> friends2) {
        Set<Long> friendsCommon = new HashSet<>();
        for (Long friendId : friends1) {
            if (friends2.contains(friendId)) {
                friendsCommon.add(friendId);
            }
        }
        return friendsCommon;
    }
}
