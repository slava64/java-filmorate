package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService service;

    @PostMapping()
    public Review post(@RequestBody Review review) {
        return service.create(review);
    }

    @PutMapping()
    public Review put(@RequestBody Review review) {
        return service.update(review);
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping()
    public Collection<Review> getAllReview(@RequestParam(defaultValue = "0") Long filmId,
                                           @RequestParam(defaultValue = "10") int count) {
        return service.getAll(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Review addLike(@PathVariable Long id, @PathVariable Long userId) {
        return service.addLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Review addDislike(@PathVariable Long id, @PathVariable Long userId) {
        return service.addDislike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Review removeLike(@PathVariable Long id, @PathVariable Long userId) {
        return service.removelike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public Review removeDislike(@PathVariable Long id, @PathVariable Long userId) {
        return service.removeDislike(id, userId);
    }


}