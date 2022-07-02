package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.Collection;

@Service
public class DirectorService {
    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorService(@Qualifier("directorDbStorage") DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public Director getDirectorById(Long id) {
        if (directorStorage.getDirectorById(id).isPresent()) {
            return directorStorage.getDirectorById(id).get();
        } else {
            throw new DirectorNotFoundException("Режиссер не найден");
        }
    }

    public Collection<Director> findAll() {
        return directorStorage.findAll();
    }

    public Director create(Director director) {
        return directorStorage.create(director);
    }

    public String delete(Long id) {
        return directorStorage.delete(id);
    }

    public Director update(Director director) {
        return directorStorage.update(director);
    }
}
