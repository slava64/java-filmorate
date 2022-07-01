package ru.yandex.practicum.filmorate.exception;

public class ReviewsNotFoundExceptions extends RuntimeException {
    public ReviewsNotFoundExceptions(String message) {
        super(message);
    }
}