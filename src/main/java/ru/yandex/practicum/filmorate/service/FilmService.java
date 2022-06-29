package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likeStorage = likeStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll().values();
    }

    public List<Film> findAllPopular(Integer count) {
        Collection<Film> films = filmStorage.findAll().values();
        return films.stream()
                .sorted((p0, p1) -> Integer.compare(p1.getLikes().size(), p0.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findOne(Long id) {
        return filmStorage.findOne(id).orElseThrow(
                () -> new FilmNotFoundException(String.format("Фильм %d не найден", id)));
    }

    public Film create(Film film) {
        checkReleaseDate(film.getReleaseDate());
        filmStorage.add(film);
        return film;
    }

    public Film update(Film film) {
        findOne(film.getId());
        checkReleaseDate(film.getReleaseDate());
        filmStorage.update(film);
        return film;
    }

    public Film delete(Long id) {
        Film film = findOne(id);
        filmStorage.delete(id);
        return film;
    }

    public Film addLike(Long id, Long userId) {
        User user = userService.findOne(userId);
        Film film = findOne(id);
        likeStorage.add(film, user);
        return film;
    }

    public Film deleteLike(Long id, Long userId) {
        User user = userService.findOne(userId);
        Film film = findOne(id);
        likeStorage.delete(film, user);
        return film;
    }

    private void checkReleaseDate(LocalDate date) {
        LocalDate minDate = LocalDate.of(1895, Month.DECEMBER, 28);
        if (minDate.isAfter(date)) {
            throw new ReleaseDateException("Дата релиза должна быть больше 28 декабря 1895 года");
        }
    }


    private Set<Film> getTopOverCrossingFilms(List<Film> films, List<Long> ids) {
        log.debug("getTopOverCrossingFilms case, films = {}, ids = {}", films.toString(), ids.toString());

        Set<Film> result = new HashSet<>();
        for (Long id: ids) {
            List<Film> tmpList = findLikedByTarget(id);
            tmpList.removeAll(films);
            result.addAll(tmpList);
        }
        return result;
    }

    private List<Long> findMatchedUsersId(List<Film> films) {
        log.debug("findMatchedUsersId case, films = {}", films.toString());

        List<Long> result = new LinkedList<>();
        Map<Long,Integer> commonLiked = new TreeMap<>();
        for (Film film: films) {
                for (Long like : film.getLikes()) {
                    if (commonLiked.containsKey(like)) {
                        commonLiked.put(like, commonLiked.get(like) + 1);
                    } else {
                        commonLiked.put(like,  1);
                    }
                }
        }
        List<Map.Entry<Long,Integer>> entries =  commonLiked.entrySet()
                .stream().filter(e -> e.getValue() > 1)
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toList());
        for (Map.Entry<Long,Integer> entry : entries) {
            result.add(entry.getKey());
        }
        return result;
    }

    private List<Film> findLikedByTarget(Long id) {
        log.debug("findLikedByTarget case, id = {}",id);

        return filmStorage.findAll()
                .values()
                .stream()
                .filter(film -> (film.getLikes() != null))
                .filter(film -> film.getLikes().contains(id))
                .collect(Collectors.toList());
    }

    public Set<Film> findRecommendations(Long id) {
        log.debug("findRecommendations case, id = {}",id);
        List<Film> films =  findLikedByTarget(id);
        List<Long> matchedUsers = findMatchedUsersId(films);
        return getTopOverCrossingFilms(films, matchedUsers);
    }

}
