package ru.yandex.practicum.filmorate.storage.dao;


import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.ReviewLike;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    Review create(Review review);

    Review update(Review review);

    Review getById (Long id);

    void remove(Long id);

    Collection<Review> getAll (Long filmId,int count);

    void addLike (Long userId, Long reviewID);

    void addDislike (Long userId, Long reviewID);

    void removeLike (Long userId, Long reviewID);

    void removeDislike (Long userId, Long reviewID);

}
