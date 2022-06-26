package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Long, Mpa> findAll() {
        return jdbcTemplate.query("select * from mpa", (ResultSet rs) -> {
            Map <Long, Mpa> results = new HashMap<>();
            while (rs.next()) {
                results.put(rs.getLong("id"), mapRow(rs, rs.getRow()));
            }
            return results;
        });
    }

    @Override
    public Optional<Mpa> findOne(Long id) {
        try {
            String sqlQuery = "select * from mpa where id = ?";
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sqlQuery, this::mapRow, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Mpa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(
                resultSet.getLong("id"),
                resultSet.getString("name")
        );
    }
}
