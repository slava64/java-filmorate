package ru.yandex.practicum.filmorate.storage.dao;


import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Optional;

public interface DirectorStorage {
    Director update(Director director);

    String delete(Long id);

    Director create(Director director);

    Collection<Director> findAll();

    Optional<Director> getDirectorById(Long id);
}
