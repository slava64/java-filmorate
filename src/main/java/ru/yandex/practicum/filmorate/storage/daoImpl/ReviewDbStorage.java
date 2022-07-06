package ru.yandex.practicum.filmorate.storage.daoImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ReviewsNotFoundExceptions;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;


@Slf4j
@AllArgsConstructor
@Component
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    private final String UPDATE = "UPDATE REVIEWS SET CONTENT=?,IS_POSITIVE=? WHERE id = ?";
    private final String GET_BY_ID = "SELECT * FROM REVIEWS WHERE ID = ?";
    private final String UP_RATE = "UPDATE REVIEWS SET RATE = RATE + 1 WHERE ID=?";
    private final String DOWN_RATE = "UPDATE REVIEWS SET RATE = RATE - 1 WHERE ID=?";
    private final String DELETE_FROM_REVIEWS_LIKES = "DELETE FROM REVIEWS_LIKES WHERE USER_ID=? AND REVIEW_ID =? AND IS_USEFUL = ?";
    private final String ADD_LIKE = "INSERT INTO REVIEWS_LIKES (REVIEW_ID,USER_ID,IS_USEFUL) VALUES (?,?,?)";
    private final String GET_ALL_BY_ID1 = "SELECT * FROM REVIEWS ORDER BY RATE DESC LIMIT ?";
    private final String GET_ALL_BY_ID2 = "SELECT * FROM REVIEWS WHERE FILM_ID=? ORDER BY RATE DESC LIMIT ?";
    private final String REMOVE_REVIEW = "DELETE FROM REVIEWS WHERE ID=?";

    @Override
    public Review create(Review review) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement reviewStatement = con.prepareStatement(
                    "INSERT INTO REVIEWS " + "(CONTENT,IS_POSITIVE,FILM_ID,USER_ID) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            reviewStatement.setString(1, review.getContent());
            reviewStatement.setBoolean(2, review.getIsPositive());
            reviewStatement.setLong(3, review.getFilmId());
            reviewStatement.setLong(4, review.getUserId());
            return reviewStatement;
        }, keyHolder);

        Review filmReview = jdbcTemplate.queryForObject(GET_BY_ID, this::reviewRows, keyHolder.getKey());

        return filmReview;

    }

    @Override
    public Review update(Review review) {

        int check = jdbcTemplate.update(UPDATE, review.getContent(), review.getIsPositive(), review.getId());

        if (check != 0) {
            return getById(review.getId());
        }
        throw new ReviewsNotFoundExceptions("Обзор не найден");
    }

    @Override
    public Review getById(Long id) {

        Review review = null;
        try {
            review = jdbcTemplate.queryForObject(GET_BY_ID, this::reviewRows, id);
        } catch (DataAccessException e) {
            throw new ReviewsNotFoundExceptions("Обзор не найден");
        }
        return review;
    }

    @Override
    public void remove(Long id) {

        jdbcTemplate.update(REMOVE_REVIEW, id);

    }

    @Override
    public Collection<Review> getAll(Long filmId, int count) {

        if (filmId > 0) {
            return jdbcTemplate.query(GET_ALL_BY_ID2, this::reviewRows, filmId, count);
        } else {
            return jdbcTemplate.query(GET_ALL_BY_ID1, this::reviewRows, count);
        }
    }

    @Override
    public void addLike(Long reviewID, Long userId) {

        jdbcTemplate.update(ADD_LIKE, reviewID, userId, true);
        jdbcTemplate.update(UP_RATE, reviewID);

    }

    @Override
    public void addDislike(Long reviewId, Long userId) {

        jdbcTemplate.update(ADD_LIKE, reviewId, userId, false);
        jdbcTemplate.update(DOWN_RATE, reviewId);

    }

    @Override
    public void removeLike(Long userId, Long reviewId) {

        jdbcTemplate.update(DELETE_FROM_REVIEWS_LIKES, userId, reviewId);
        jdbcTemplate.update(DOWN_RATE, reviewId, true);

    }

    @Override
    public void removeDislike(Long userId, Long reviewId) {

        jdbcTemplate.update(DELETE_FROM_REVIEWS_LIKES, userId, reviewId);
        jdbcTemplate.update(UP_RATE, reviewId, false);

    }

    private Review reviewRows(ResultSet rowSet, int rowNum) throws SQLException {

        return Review.builder()
                .id((long) rowSet.getInt("id"))
                .content(rowSet.getString("content"))
                .isPositive(rowSet.getBoolean("is_positive"))
                .userId((long) rowSet.getInt("user_id"))
                .filmId((long) rowSet.getInt("film_id"))
                .useful(rowSet.getInt("rate"))
                .build();
    }


}
