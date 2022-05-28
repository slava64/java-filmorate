package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendsNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
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
        checkUser(id);
        return userStorage.findOne(id);
    }

    public List<User> findAllFriends(Long id) {
        checkUser(id);
        //checkFriends(id);
        User user = userStorage.findOne(id);
        Collection<User> users = userStorage.findAll().values();
        return users
                .stream()
                .filter(x -> user.getFriends().contains(x.getId()))
                .collect(Collectors.toList());
    }

    public List<User> findAllFriendsCommon(Long id, Long friendId) {
        checkUser(id);
        checkUser(friendId);
        //checkFriends(id);
        //checkFriends(friendId);
        User user = userStorage.findOne(id);
        User friend = userStorage.findOne(friendId);
        Set<Long> friendsCommon = getSetFriendsCommon(user.getFriends(), friend.getFriends());
        Collection<User> users = userStorage.findAll().values();
        return users
                .stream()
                .filter(x -> friendsCommon.contains(x.getId()))
                .collect(Collectors.toList());
    }

    public User create(User user) {
        userStorage.add(user);
        return user;
    }

    public User update(@Valid User user) {
        checkUser(user.getId());
        userStorage.update(user);
        return user;
    }

    public User delete(@Valid User user) {
        checkUser(user.getId());
        userStorage.delete(user);
        return user;
    }

    public User addFriend(Long id, Long friendId) {
        checkUser(id);
        checkUser(friendId);
        return userStorage.addFriend(id, friendId);
    }

    public User deleteFriend(Long id, Long friendId) {
        checkUser(id);
        checkUser(friendId);
        return userStorage.deleteFriend(id, friendId);
    }

    private void checkUser(Long id) {
        if (userStorage.findOne(id) == null) {
            throw new UserNotFoundException(String.format("Пользователь %d не найден", id));
        }
    }

    private void checkFriends(Long userId) {
        if (userStorage.findOne(userId).getFriends().size() == 0) {
            throw new FriendsNotFoundException(String.format("Друзья у пользователя %d не найдены", userId));
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
