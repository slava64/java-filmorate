package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;


    @Autowired
    public UserService(UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
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
        User user = findOne(id);
        userStorage.delete(id);
        return user;
    }

    public User addFriend(Long id, Long friendId) {
        User user = findOne(id);
        User friend = findOne(friendId);
        friendStorage.add(user, friend);
        return user;
    }

    public User deleteFriend(Long id, Long friendId) {
        User user = findOne(id);
        User friend = findOne(friendId);
        friendStorage.delete(user, friend);
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
