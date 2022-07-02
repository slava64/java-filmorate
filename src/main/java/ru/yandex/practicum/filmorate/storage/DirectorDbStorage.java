package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Optional;

@Component
@Primary
public class DirectorDbStorage implements DirectorStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director create(Director director) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("directors").usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", director.getName());

        Number num = jdbcInsert.executeAndReturnKey(parameters);
        director.setId(num.longValue());

        return director;
    }

    @Override
    public Optional<Director> getDirectorById(Long id) {
        String sql = "select * from directors where id=?";

        return Optional.ofNullable(jdbcTemplate.query(
                sql,
                rs -> rs.next() ? new DirectorMapper().mapRow(rs, 1) : null,
                id));
    }

    @Override
    public Director update(Director director) {
        Optional<Director> dir = getDirectorById(director.getId());
        if (dir.isPresent()) {
            String sql = "UPDATE directors set name=? where id=?";
            jdbcTemplate.update(sql, director.getName(), director.getId());
            return director;
        } else {
            throw new DirectorNotFoundException("Режиссер не найден");
        }
    }

    @Override
    public String delete(Long id) {
        String name = getDirectorById(id).get().getName();

        String sql = "delete from directors cascade WHERE id = ?";
        jdbcTemplate.update(sql, id);

        return name;
    }

    @Override
    public Collection<Director> findAll() {
        String sql = "select * from directors";
        return jdbcTemplate.query(sql, new DirectorMapper());
    }
}
