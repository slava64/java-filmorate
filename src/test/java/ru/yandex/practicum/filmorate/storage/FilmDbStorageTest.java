package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.daoImpl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.daoImpl.MpaDbStorage;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final MpaDbStorage mpaStorage;

    @Test
    @Order(1)
    public void testAdd_1() {
        Mpa mpa = mpaStorage.findOne((long) 1).orElse(null);
        assertThat(mpa).isNotNull();

        Film film = new Film(
                (long) 1,
                "New film",
                "New Description",
                LocalDate.of(3000, 8, 1),
                1,
                4
        );
        film.setMpa(mpa);

        filmStorage.add(film);
        Film addedFilm = filmStorage.findOne((long) 1).orElse(null);

        assertThat(addedFilm).isNotNull();
        assertThat(addedFilm.getId()).isEqualTo(film.getId());
        assertThat(addedFilm.getName()).isEqualTo(film.getName());
        assertThat(addedFilm.getDescription()).isEqualTo(film.getDescription());
        assertThat(addedFilm.getReleaseDate()).isEqualTo(film.getReleaseDate());
        assertThat(addedFilm.getDuration()).isEqualTo(film.getDuration());
        assertThat(addedFilm.getRate()).isEqualTo(film.getRate());
        assertThat(addedFilm.getMpa()).isEqualTo(film.getMpa());
    }

    @Test
    @Order(2)
    public void testUpdate_2() {
        Mpa mpa = mpaStorage.findOne((long) 2).orElse(null);
        assertThat(mpa).isNotNull();

        Film film = new Film(
                (long) 1,
                "Update film",
                "Update Description",
                LocalDate.of(3001, 8, 1),
                2,
                5
        );
        film.setMpa(mpa);

        filmStorage.update(film);
        Film updatedFilm = filmStorage.findOne((long) 1).orElse(null);

        assertThat(updatedFilm).isNotNull();
        assertThat(updatedFilm.getId()).isEqualTo(film.getId());
        assertThat(updatedFilm.getName()).isEqualTo(film.getName());
        assertThat(updatedFilm.getDescription()).isEqualTo(film.getDescription());
        assertThat(updatedFilm.getReleaseDate()).isEqualTo(film.getReleaseDate());
        assertThat(updatedFilm.getDuration()).isEqualTo(film.getDuration());
        assertThat(updatedFilm.getRate()).isEqualTo(film.getRate());
        assertThat(updatedFilm.getMpa()).isEqualTo(film.getMpa());
    }

    @Test
    @Order(3)
    public void testFindOne_3() {
        Film film = filmStorage.findOne((long) 1).orElse(null);

        assertThat(film).isNotNull();
        assertThat(film.getId()).isEqualTo(1);
    }

    @Test
    @Order(4)
    public void testFindAll_4() {
        Mpa mpa = mpaStorage.findOne((long) 1).orElse(null);
        assertThat(mpa).isNotNull();

        Film film = new Film(
                (long) 2,
                "New film 2",
                "New Description 2",
                LocalDate.of(3000, 8, 1),
                1,
                4
        );
        film.setMpa(mpa);

        filmStorage.add(film);


        Map<Long, Film> films = filmStorage.findAll();

        assertThat(films).isNotNull();
        assertThat(films.size()).isEqualTo(2);
    }

    @Test
    @Order(5)
    public void testDelete_5() {
        Film film = filmStorage.findOne((long) 2).orElse(null);
        assertThat(film).isNotNull();

        filmStorage.delete((long) 2);
        Film film2 = filmStorage.findOne((long) 2).orElse(null);
        assertThat(film2).isNull();
    }
}
