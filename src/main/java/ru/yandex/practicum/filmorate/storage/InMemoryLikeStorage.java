package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryLikeStorage implements LikeStorage {
    @Override
    public void add(Film film, User user) {
        if (film.getLikes() != null) {
            film.getLikes().add(user.getId());
        } else {
            Set<Long> likes = new HashSet<>();
            likes.add(user.getId());
            film.setLikes(likes);
        }
    }

    @Override
    public Boolean delete(Film film, User user) {
        if (film.getLikes() != null) {
            film.getLikes().remove(user.getId());
        }
        return true;
    }
}
