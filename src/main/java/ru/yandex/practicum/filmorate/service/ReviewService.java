package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.EventDbStorage;
import ru.yandex.practicum.filmorate.storage.ReviewDbStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewDbStorage storage;

    public Review create(Review review) {
        Review createdReview = storage.create(review);
        EventDbStorage.addEvent(
                review.getUserId(),
                review.getId(),
                Event.EventType.REVIEW,
                Event.EventOperation.ADD
        );
        return createdReview;
    }

    public Review update(Review review) {
        Review updatedReview = storage.update(review);
        EventDbStorage.addEvent(
                review.getUserId(),
                review.getId(),
                Event.EventType.REVIEW,
                Event.EventOperation.UPDATE
        );
        return updatedReview;
    }

    public Review getById(Long id) {
        return storage.getById(id);
    }

    public void deleteById(Long id) {
        storage.remove(id);
        Review review = storage.getById(id);
        EventDbStorage.addEvent(
                review.getUserId(),
                review.getId(),
                Event.EventType.REVIEW,
                Event.EventOperation.REMOVE
        );
    }

    public Collection<Review> getAll(Long id, int count) {

        return storage.getAll(id, count);
    }

    public Review addLike (Long reviewId,Long userId ) {

        return storage.addLike(reviewId,userId);
    }

    public Review addDislike (Long reviewId,Long userId ) {

        return storage.addDislike(reviewId,userId);
    }

    public Review removeDislike (Long reviewId,Long userId ) {

        return storage.removeDislike(reviewId,userId);
    }

    public Review removelike (Long reviewId,Long userId ) {

        return storage.removeLike(reviewId,userId);
    }

}
