package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    String GET_LIKE_SORTED_FILM_BY_DIRECTOR_ID_SQL = "SELECT df.film_id  " +
            "FROM DIRECTOR_FILM df " +
            "LEFT JOIN films f ON df.FILM_ID = f.ID " +
            "LEFT JOIN LIKES l ON df.FILM_ID = l.FILM_ID " +
            "WHERE df.DIRECTOR_ID = ? " +
            "GROUP BY df.FILM_ID " +
            "ORDER BY COUNT(l.user_id) ASC";

    String GET_YEAR_SORTED_FILM_BY_DIRECTOR_ID_SQL = "SELECT df.film_id FROM DIRECTOR_FILM df " +
            "LEFT JOIN films f ON df.FILM_ID = f.ID " +
            "WHERE  df.director_id =? " +
            "ORDER BY f.RELEASE_DATE ";

    String GET_LIST_DIRECTORS_BY_FILM_ID_SQL = "select df.DIRECTOR_ID as id, d.name " +
            "from director_film df " +
            "left join directors d on df.director_id = d.id " +
            "where df.film_id=? group by df.film_id";

    private final JdbcTemplate jdbcTemplate;
    private Long id = Long.valueOf(1);

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Film film) {
        String sqlQuery = "insert into films(name, description, release_date, duration, rate, mpa_id) " +
                "values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId()
        );
        film.setId(id++);
        updateGenres(film);
        updateDirectors(film);
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, rate =?, mpa_id = ?" +
                "where id = ?";
        jdbcTemplate.update(
                sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId()
        );
        updateGenres(film);
        updateDirectors(film);
    }


    @Override
    public Boolean delete(Long id) {
        return jdbcTemplate.update("delete from films where id = ?", id) > 0;
    }

    @Override
    public Map<Long, Film> findAll() {

        return jdbcTemplate.query("select " +
                        "f.id, " +
                        "f.name, " +
                        "f.description, " +
                        "f.release_date, " +
                        "f.duration, " +
                        "f.rate, " +
                        "m.id as mpa_id, " +
                        "m.name as mpa_name, " +
                        "l.user_id as like_user_id, " +
                        "g.id as genre_id, " +
                        "g.name as genre_name " +
                        "from films as f " +
                        "left join mpa as m " +
                        "on f.mpa_id = m.id " +
                        "left join likes as l " +
                        "on f.id = l.film_id " +
                        "left join films_genres as fg " +
                        "on f.id = fg.film_id " +
                        "left join genres as g " +
                        "on fg.genre_id  = g.id "
                , (ResultSet rs) -> {
                    Map<Long, Film> results = new HashMap<>();
                    while (rs.next()) {
                        if (results.containsKey(rs.getLong("id"))) {
                            Film film = results.get(rs.getLong("id"));
                            if (film.getLikes() != null) {
                                film.getLikes().add(rs.getLong("like_user_id"));
                            }
                            if (film.getGenres() != null) {
                                Genre genre = new Genre(
                                        rs.getLong("genre_id"),
                                        rs.getString("genre_name")
                                );

                                film.getGenres().add(genre);
                            }
                            film.setDirectors(getListDirectorsByFilmId(rs.getLong("id")));

                        } else {
                            results.put(rs.getLong("id"), mapRow(rs, rs.getRow()));
                        }
                    }
                    return results;
                });
    }
    @Override
    public Map<Long, Film> getPopularFilms(Integer count, Integer genreId, Integer year) {
        String sqlGetFilmsWithGenre = "select f.id, f.name, f.description,  f.release_date,  f.duration,  f.rate, " +
                " m.id as mpa_id, m.name as mpa_name, l.user_id as like_user_id, g.id as genre_id,  g.name as genre_name" +
                " from films as f" +
                " left join mpa as m on f.mpa_id = m.id " +
                " left join likes as l on f.id = l.film_id " +
                " left join films_genres as fg on f.id = fg.film_id " +
                " left join genres as g on fg.genre_id  = g.id" +
                " WHERE fg.genre_id = ?";
        String sqlGetFilmsForYear = "select f.id, f.name, f.description,  f.release_date,  f.duration,  f.rate, " +
                " m.id as mpa_id, m.name as mpa_name, l.user_id as like_user_id, g.id as genre_id,  g.name as genre_name" +
                " from films as f" +
                " left join mpa as m on f.mpa_id = m.id " +
                " left join likes as l on f.id = l.film_id " +
                " left join films_genres as fg on f.id = fg.film_id " +
                " left join genres as g on fg.genre_id  = g.id" +
                " WHERE extract(year from f.RELEASE_DATE) = ?";
        if (Objects.nonNull(year)) {
            return jdbcTemplate.query(sqlGetFilmsForYear, (ResultSet rs) -> {
                Map<Long, Film> results = new HashMap<>();
                while (rs.next()) {
                    if (results.containsKey(rs.getLong("id"))) {
                        Film film = results.get(rs.getLong("id"));
                        if (film.getLikes() != null) {
                            film.getLikes().add(rs.getLong("like_user_id"));
                        }
                        Genre genre = new Genre(
                                rs.getLong("genre_id"),
                                rs.getString("genre_name")
                        );
                        film.getGenres().add(genre);
                    } else {
                        results.put(rs.getLong("id"), mapRow(rs, rs.getRow()));
                    }
                }
                return results;
            }, year);
        } else {
            return jdbcTemplate.query(sqlGetFilmsWithGenre, (ResultSet rs) -> {
                Map<Long, Film> results = new HashMap<>();
                while (rs.next()) {
                    if (results.containsKey(rs.getLong("id"))) {
                        Film film = results.get(rs.getLong("id"));
                        if (film.getLikes() != null) {
                            film.getLikes().add(rs.getLong("like_user_id"));
                        }
                        Genre genre = new Genre(
                                rs.getLong("genre_id"),
                                rs.getString("genre_name")
                        );
                        film.getGenres().add(genre);
                    } else {
                        results.put(rs.getLong("id"), mapRow(rs, rs.getRow()));
                    }
                }
                return results;
            }, genreId);
        }
    }
    @Override
    public Optional<Film> findOne(Long id) {
        try {
            String sqlQuery = "select " +
                    "f.id, " +
                    "f.name, " +
                    "f.description, " +
                    "f.release_date, " +
                    "f.duration, " +
                    "f.rate, " +
                    "m.id as mpa_id, " +
                    "m.name as mpa_name, " +
                    "l.user_id as like_user_id, " +
                    "g.id as genre_id, " +
                    "g.name as genre_name " +
                    "from films as f " +
                    "left join mpa as m " +
                    "on f.mpa_id = m.id " +
                    "left join likes as l " +
                    "on f.id = l.film_id " +
                    "left join films_genres as fg " +
                    "on f.id = fg.film_id " +
                    "left join genres as g " +
                    "on fg.genre_id  = g.id " +
                    "where f.id = ?" +
                    "order by g.id";
            return jdbcTemplate.query(sqlQuery, (ResultSet rs) -> {
                if (rs.next()) {
                    Film film = mapRow(rs, rs.getRow());
                    while (rs.next()) {
                        if (film.getLikes() != null) {
                            film.getLikes().add(rs.getLong("like_user_id"));
                        }
                        Genre genre = new Genre(
                                rs.getLong("genre_id"),
                                rs.getString("genre_name")
                        );
                        if (film.getGenres() != null) {
                            film.getGenres().add(genre);
                        }
                    }

                    film.setDirectors(getListDirectorsByFilmId(film.getId()));

                    return Optional.of(film);
                }
                return Optional.empty();
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getInt("duration"),
                resultSet.getInt("rate")
        );

        Mpa mpa = new Mpa(resultSet.getLong("mpa_id"), resultSet.getString("mpa_name"));
        film.setMpa(mpa);

        long likeUserId = resultSet.getLong("like_user_id");
        if (likeUserId != 0) {
            Set<Long> likes = new HashSet<>();
            likes.add(likeUserId);
            film.setLikes(likes);
        }

        long genreId = resultSet.getLong("genre_id");
        if (genreId != 0) {
            Genre genre = new Genre(
                    resultSet.getLong("genre_id"),
                    resultSet.getString("genre_name")
            );
            Set<Genre> genres = new HashSet<>();
            genres.add(genre);
            film.setGenres(genres);
        }

        film.setDirectors(getListDirectorsByFilmId(film.getId()));

        return film;
    }

    // Обновляет жанры
    private void updateGenres(Film film) {
        if (film.getGenres() != null) {
            jdbcTemplate.update("delete from films_genres where film_id = ?", film.getId());
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(
                        "insert into films_genres(film_id, genre_id) " +
                                "values (?, ?)",
                        film.getId(),
                        genre.getId()
                );
            }
            film.setGenres(new TreeSet<>(film.getGenres()));
        }
    }

    private void updateDirectors(Film film) {
        if (film.getDirectors() != null && film.getDirectors().size() > 0) {
            jdbcTemplate.update("delete from director_film where film_id = ?", film.getId());
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update(
                        "insert into director_film(director_id, film_id) " +
                                "values (?, ?)",
                        director.getId(),
                        film.getId()
                );
            }
            film.setDirectors(new ArrayList<>(film.getDirectors()));
        } else {
            jdbcTemplate.update("delete from director_film where film_id = ?", film.getId());
            film.setDirectors(null);
        }
    }

    @Override
    public Collection<Film> getYearSortedFilmsByDirectorId(long id) {

        List<Long> filmsId = jdbcTemplate.query(GET_YEAR_SORTED_FILM_BY_DIRECTOR_ID_SQL, (rs, rowNum) -> rs.getLong("film_id"), id);

        List<Film> films = new ArrayList<>();

        for (long l : filmsId) {
            if (findOne(l).isPresent()) {
                films.add(findOne(l).get());
            }
        }

        return films;
    }

    @Override
    public List<Film> getLikeSortedFilmsByDirectorId(long id) {

        List<Long> filmsId = jdbcTemplate.query(GET_LIKE_SORTED_FILM_BY_DIRECTOR_ID_SQL, (rs, rowNum) -> rs.getLong("film_id"), id);

        List<Film> films = new ArrayList<>();

        for (long l : filmsId) {
            if (findOne(l).isPresent()) {
                films.add(findOne(l).get());
            }
        }

        if (films.size() == 0) {
            throw new FilmNotFoundException("Проверьте правильно ли указано имя режиссера." +
                    " Фильмов такого режиссера не найдено.");
        }

        return films;
    }

    private List<Director> getListDirectorsByFilmId (long id) {
        return jdbcTemplate.query(GET_LIST_DIRECTORS_BY_FILM_ID_SQL, new DirectorMapper(), id);
    }
}
