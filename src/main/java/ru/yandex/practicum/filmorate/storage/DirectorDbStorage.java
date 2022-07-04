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
    String GET_BY_ID_SQL = "select * from directors where id=?";
    String UPDATE_DIRECTOR_SQL = "UPDATE directors set name=? where id=?";
    String DELETE_DIRECTOR_SQL = "delete from directors cascade WHERE id = ?";
    String FIND_ALL_SQL = "select * from directors";
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

        return Optional.ofNullable(jdbcTemplate.query(
                GET_BY_ID_SQL,
                rs -> rs.next() ? new DirectorMapper().mapRow(rs, 1) : null,
                id));
    }

    @Override
    public Director update(Director director) {
        Optional<Director> dir = getDirectorById(director.getId());
        if (dir.isPresent()) {
            jdbcTemplate.update(UPDATE_DIRECTOR_SQL, director.getName(), director.getId());
            return director;
        } else {
            throw new DirectorNotFoundException("Режиссер не найден");
        }
    }

    @Override
    public String delete(Long id) {
        String name = getDirectorById(id).get().getName();
        jdbcTemplate.update(DELETE_DIRECTOR_SQL, id);

        return name;
    }

    @Override
    public Collection<Director> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, new DirectorMapper());
    }
}
