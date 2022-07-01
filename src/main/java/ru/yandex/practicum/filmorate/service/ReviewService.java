package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventDbStorage;

import java.util.Collection;

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
        Review createdReview = reviewStorage.create(review);
        EventDbStorage.addEvent(
                createdReview.getUserId(),
                createdReview.getId(),
                Event.EventType.REVIEW,
                Event.EventOperation.ADD
        );
        return createdReview;
    }

    public Review update(Review review) {
        Review updatedReview = reviewStorage.update(review);
        EventDbStorage.addEvent(
                updatedReview.getUserId(),
                updatedReview.getId(),
                Event.EventType.REVIEW,
                Event.EventOperation.UPDATE
        );
        return updatedReview;
    }

    public Review getById(Long id) {

        return reviewStorage.getById(id);
    }

    public void deleteById(Long id) {
        Review review = reviewStorage.getById(id);
        reviewStorage.remove(id);
        EventDbStorage.addEvent(
                review.getUserId(),
                review.getId(),
                Event.EventType.REVIEW,
                Event.EventOperation.REMOVE
        );
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
