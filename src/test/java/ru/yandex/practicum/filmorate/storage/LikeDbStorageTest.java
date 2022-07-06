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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.daoImpl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.daoImpl.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.daoImpl.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.daoImpl.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LikeDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final MpaDbStorage mpaStorage;
    private final UserDbStorage userStorage;
    private final LikeDbStorage likeStorage;

    @Test
    @Order(1)
    public void testAddLike_1() {
        User user = new User(
                (long) 1,
                "test@test.com",
                "test",
                "Name",
                LocalDate.of(1991, 6, 1)
        );

        userStorage.add(user);

        User addedUser = userStorage.findOne((long) 1).orElse(null);
        assertThat(addedUser).isNotNull();

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

        likeStorage.add(addedFilm, addedUser);

        Film likedFilm = filmStorage.findOne((long) 1).orElse(null);
        assertThat(likedFilm).isNotNull();
        assertThat(likedFilm.getLikes()).isNotNull();
        assertThat(likedFilm.getLikes().size()).isEqualTo(1);
    }

    @Test
    @Order(2)
    public void testDeleteLike_2() {
        User user = userStorage.findOne((long) 1).orElse(null);
        assertThat(user).isNotNull();

        Film film = filmStorage.findOne((long) 1).orElse(null);
        assertThat(film).isNotNull();

        likeStorage.delete(film, user);

        Film likedFilm = filmStorage.findOne((long) 1).orElse(null);
        assertThat(film).isNotNull();
        assertThat(likedFilm.getLikes()).isNotNull();
        assertThat(likedFilm.getLikes().size()).isEqualTo(0);
    }
}
