package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewDbStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewDbStorage storage;

    public Review create(Review review) {
        return storage.create(review);
    }

    public Review update(Review review) {
        return storage.update(review);
    }

    public Review getById(Long id) {
        return storage.getById(id);
    }

    public void deleteById(Long id) {

        storage.remove(id);
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
