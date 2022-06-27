package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
@Primary
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(User user, User friend) {
        String sqlQuery = "insert into friends(user_id, friend_id) " +
                "values (?, ?)";
        jdbcTemplate.update(
                sqlQuery,
                user.getId(),
                friend.getId()
        );
        user.getFriends().add(friend.getId());
    }

    @Override
    public Boolean delete(User user, User friend) {
        user.getFriends().remove(friend.getId());
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        return jdbcTemplate.update(sqlQuery, user.getId(), friend.getId()) > 0;
    }
}
