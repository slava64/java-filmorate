package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;
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
    private final String upRate = "UPDATE REVIEWS SET RATE = RATE + 1 WHERE ID=?";
    private final String downRate = "UPDATE REVIEWS SET RATE = RATE - 1 WHERE ID=?";
    private final String delete = "DELETE FROM REVIEWS_LIKES WHERE USER_ID=? AND REVIEW_ID =? AND IS_USEFUL = ?";
    private final String addLike = "INSERT INTO REVIEWS_LIKES (REVIEW_ID,USER_ID,IS_USEFUL) VALUES (?,?,?)";

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

        String sql = "SELECT * FROM REVIEWS WHERE ID = ?";

        Review filmReview = jdbcTemplate.queryForObject(sql, this::reviewRows, keyHolder.getKey());

        return filmReview;
    }

    @Override
    public Review update(Review review) {

        String sql = "UPDATE REVIEWS SET CONTENT=?,IS_POSITIVE=? WHERE id = ?";

        int check = jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getId());

        if (check != 0) {
            return getById(review.getId());
        }
        return null;
    }

    @Override
    public Review getById(Long id) {

        String getReview = "SELECT * FROM REVIEWS WHERE ID=?";

        try {
            return jdbcTemplate.queryForObject(getReview, this::reviewRows, id);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void remove(Long id) {

        String delete = "DELETE FROM REVIEWS WHERE ID=?";
        jdbcTemplate.update(delete, id);
    }

    @Override
    public Collection<Review> getAll(Long filmId, int count) {

        String getAllById1 = "SELECT * FROM REVIEWS ORDER BY RATE DESC LIMIT ?";
        String getAllById2 = "SELECT * FROM REVIEWS WHERE FILM_ID=? ORDER BY RATE DESC LIMIT ?";

        if (filmId > 0) {
            return jdbcTemplate.query(getAllById2, this::reviewRows, filmId, count);
        } else {
            return jdbcTemplate.query(getAllById1, this::reviewRows, count);
        }
    }

    @Override
    public Review addLike(Long reviewID, Long userId) {

        jdbcTemplate.update(addLike, reviewID, userId, true);
        jdbcTemplate.update(upRate, reviewID);

        return getById(reviewID);
    }

    @Override
    public Review addDislike(Long reviewId, Long userId) {

        jdbcTemplate.update(addLike, reviewId, userId, false);
        jdbcTemplate.update(downRate, reviewId);

        return getById(reviewId);
    }

    @Override
    public Review removeLike(Long userId, Long reviewId) {

        jdbcTemplate.update(delete, userId, reviewId);
        jdbcTemplate.update(downRate, reviewId, true);
        return getById(reviewId);
    }

    @Override
    public Review removeDislike(Long userId, Long reviewId) {

        jdbcTemplate.update(delete, userId, reviewId);
        jdbcTemplate.update(upRate, reviewId, false);
        return getById(reviewId);
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
