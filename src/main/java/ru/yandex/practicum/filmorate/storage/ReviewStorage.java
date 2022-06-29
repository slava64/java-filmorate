package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.ReviewLike;

import java.util.Collection;

public interface ReviewStorage {

    Review create(Review review);

    Review update(Review review);

    Review getById (Long id);

    void remove(Long id);

    Collection<Review> getAll (Long filmId,int count);

    Review addLike (Long userId, Long reviewID);

    Review addDislike (Long userId, Long reviewID);

    Review removeLike (Long userId, Long reviewID);

    Review removeDislike (Long userId, Long reviewID);
}
