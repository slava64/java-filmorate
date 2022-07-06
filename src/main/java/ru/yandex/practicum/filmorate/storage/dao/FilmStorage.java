package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {
    // Добавляет новый фильм
    public void add(Film film);
    // Обновляет фильм
    public void update(Film film);
    // Удаляет фильм
    public Boolean delete(Long id);
    // Возвращает все фильмы
    public Map<Long, Film> findAll();
    // Возвращает один фильм
    public Optional<Film> findOne(Long id);
    // Возвращает фильмы по id режисера отсортированные по году
    Collection<Film> getYearSortedFilmsByDirectorId(long id);
    // Возвращает фильмы по id режисера отсортированные по лайкам
    Collection<Film> getLikeSortedFilmsByDirectorId(long id);
    //возвращает популярные фильмы по году и жанру
    Map<Long, Film> getPopularFilms(Integer count, Integer genreId, Integer year);
    //возвращает список фильмов, где имя режиссера и название фильма содержат подстроку
    Collection<Film> getFilmsWhereDirectorNameAndFilmTitleContainsQuery(String query);
    //возвращает список фильмов, где имя режиссера содержат подстроку
    Collection<Film> getFilmsWhereDirectorNameContainsQuery(String query);
    //возвращает список фильмов, где название фильма содержат подстроку
    Collection<Film> getFilmsWhereFilmTitleContainsQuery(String query);
}
