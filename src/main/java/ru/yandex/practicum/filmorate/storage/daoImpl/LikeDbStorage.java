package ru.yandex.practicum.filmorate.storage.daoImpl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.LikeStorage;

import java.util.HashSet;
import java.util.Set;

@Component
@Primary
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Film film, User user) {
        if (film.getLikes() != null) {
            film.getLikes().add(user.getId());
        } else {
            Set<Long> likes = new HashSet<>();
            likes.add(user.getId());
            film.setLikes(likes);
        }
        String sqlQuery = "insert into likes(film_id, user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(
                sqlQuery,
                film.getId(),
                user.getId()
        );
    }

    @Override
    public Boolean delete(Film film, User user) {
        if (film.getLikes() != null) {
            film.getLikes().remove(user.getId());
        }
        return jdbcTemplate.update(
                "delete from likes where film_id = ? and user_id = ?",
                film.getId(),
                user.getId()
        ) > 0;
    }
}
