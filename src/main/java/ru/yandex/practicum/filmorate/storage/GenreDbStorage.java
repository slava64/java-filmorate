package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Long, Genre> findAll() {
        return jdbcTemplate.query("select * from genres", (ResultSet rs) -> {
            Map <Long, Genre> results = new HashMap<>();
            while (rs.next()) {
                results.put(rs.getLong("id"), mapRow(rs, rs.getRow()));
            }
            return results;
        });
    }

    @Override
    public Optional<Genre> findOne(Long id) {
        try {
            String sqlQuery = "select * from genres where id = ?";
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sqlQuery, this::mapRow, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Genre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(
                resultSet.getLong("id"),
                resultSet.getString("name")
        );
    }
}
