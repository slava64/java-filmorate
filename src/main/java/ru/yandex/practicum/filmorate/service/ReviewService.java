package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewsNotFoundExceptions;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public Review create(Review review) {

        if (userStorage.findOne(review.getUserId()).isEmpty()) {
            throw new UserNotFoundException((String.format("Пользователь %d не найден", review.getUserId())));
        } else if (filmStorage.findOne(review.getFilmId()).isEmpty()) {
            throw new FilmNotFoundException((String.format("Фильм не найден %d не найден", review.getUserId())));
        }

        return reviewStorage.create(review);
    }

    public Review update(Review review) {
        return reviewStorage.update(review);
    }

    public Review getById(Long id) {

        return reviewStorage.getById(id);
    }

    public void deleteById(Long id) {

        reviewStorage.remove(id);
    }

    public Collection<Review> getAll(Long id, int count) {

        return reviewStorage.getAll(id, count);
    }

    public void addLike(Long reviewId, Long userId) {

         reviewStorage.addLike(reviewId, userId);
    }

    public void addDislike(Long reviewId, Long userId) {

         reviewStorage.addDislike(reviewId, userId);
    }

    public void removeDislike(Long reviewId, Long userId) {

        reviewStorage.removeDislike(reviewId, userId);
    }

    public void removelike(Long reviewId, Long userId) {

         reviewStorage.removeLike(reviewId, userId);
    }

}
