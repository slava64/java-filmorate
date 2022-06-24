package ru.yandex.practicum.filmorate.exception;

public class ReleaseDateException extends RuntimeException {
    public ReleaseDateException(String message) {
        super(message);
    }
}