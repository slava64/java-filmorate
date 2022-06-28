package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

public interface ReviewStorage {

    Review create(Review review);

    Review update(Review review);

    void remove(int id);

}
