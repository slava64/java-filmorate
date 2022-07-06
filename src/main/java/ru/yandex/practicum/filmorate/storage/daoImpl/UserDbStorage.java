package ru.yandex.practicum.filmorate.storage.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private Long id = Long.valueOf(1);

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(User user) {
        String sqlQuery = "insert into users(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery);
            int index = 1;
            ps.setString(index++, user.getEmail());
            ps.setString(index++, user.getLogin());
            ps.setString(index++, user.getName());
            ps.setDate(index++, new Date(Timestamp.valueOf(user.getBirthday().atStartOfDay()).getTime()));
            return ps;
        }, keyHolder);
        //user.setId((long) keyHolder.getKey()); не знаю почему, но не работает getKey() возвращает null
        user.setId(id++);
    }

    @Override
    public void update(User user) {
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where id = ?";
        jdbcTemplate.update(
                sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
    }

    @Override
    public Boolean delete(Long id) {
        String sqlQuery = "delete from users where id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public Map<Long, User> findAll() {
        return jdbcTemplate.query("select u.id, u.email, u.login, u.name, u.birthday, f.friend_id " +
                    "from users as u " +
                    "left join friends as f " +
                    "on u.id = f.user_id", (ResultSet rs) -> {
                Map <Long, User> results = new HashMap<>();
                while (rs.next()) {
                    if (results.containsKey(rs.getLong("id"))) {
                        User user = results.get(rs.getLong("id"));
                        user.getFriends().add(rs.getLong("friend_id"));
                    } else {
                        results.put(rs.getLong("id"), mapRowWithFriends(rs, rs.getRow()));
                    }
                }
            return results;
        });
    }

    @Override
    public Optional<User> findOne(Long id) {
        try {
            String sqlQuery = "select u.id, u.email, u.login, u.name, u.birthday, f.friend_id " +
                    "from users as u " +
                    "left join friends as f " +
                    "on u.id = f.user_id " +
                    "where u.id = ?";
            return jdbcTemplate.query(sqlQuery, (ResultSet rs) -> {
                if (rs.next()) {
                    User user = mapRowWithFriends(rs, rs.getRow());
                    while (rs.next()) {
                        user.getFriends().add(rs.getLong("friend_id"));
                    }
                    return Optional.of(user);
                }
                return Optional.empty();
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private User mapRowWithFriends(ResultSet rs, int rowNum) throws SQLException {
        Set<Long> friends = new HashSet<>();
        long friendId = rs.getLong("friend_id");
        if (friendId != 0) {
            friends.add(friendId);
        }
        User user = new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate()
        );
        user.setFriends(friends);
        return user;
    }
}
