package ru.yandex.practicum.filmorate.storage.daoImpl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendStorage;

@Component
public class InMemoryFriendStorage implements FriendStorage {
    @Override
    public void add(User user, User friend) {
        user.getFriends().add(friend.getId());
    }

    @Override
    public Boolean delete(User user, User friend) {
        user.getFriends().remove(friend.getId());
        return true;
    }
}
