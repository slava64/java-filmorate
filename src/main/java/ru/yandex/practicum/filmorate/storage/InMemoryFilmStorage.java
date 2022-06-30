package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    public final Map<Long, Film> films = new HashMap<>();
    private Long id = Long.valueOf(1);

    @Override
    public void add(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public Boolean delete(Long id) {
        films.remove(id);
        return true;
    }

    @Override
    public Map<Long, Film> findAll() {
        return films;
    }

    @Override
    public Optional<Film> findOne(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Collection<Film> getYearSortedFilmsByDirectorId(long id) {
        List<Film> sortedFilms = getFilmsByDirectorId(id);
        if(sortedFilms == null || sortedFilms.size() == 0) {
            return null;
        } else {
            return sortedFilms.stream()
                    .sorted(this::compareYear)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<Film> getLikeSortedFilmsByDirectorId(long id) {
        List<Film> sortedFilms = getFilmsByDirectorId(id);
        if(sortedFilms == null || sortedFilms.size() == 0) {
            return null;
        } else {
            return sortedFilms.stream()
                    .sorted(this::compareLike)
                    .collect(Collectors.toList());
        }
    }

    private int compareLike(Film f0, Film f1) {
        return  f0.getLikes().size() - f1.getLikes().size();
    }

    private int compareYear(Film f0, Film f1) {
        return f0.getReleaseDate().getYear() - f1.getReleaseDate().getYear();
    }

    private List<Film> getFilmsByDirectorId(long id) {
        List<Film> sortedFilms = null;
        for (Film film : films.values()) {
            for(Director d : film.getDirectors()) {
                if(d.getId() == id) {
                    sortedFilms.add(film);
                }
            }
        }
        return sortedFilms;
    }
}
