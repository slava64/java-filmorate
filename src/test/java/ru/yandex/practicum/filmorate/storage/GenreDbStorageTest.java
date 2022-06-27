package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GenreDbStorageTest {
    private final GenreDbStorage genreStorage;

    @Test
    @Order(1)
    public void testFindOne_1() {
        Genre genre = genreStorage.findOne((long) 1).orElse(null);

        assertThat(genre).isNotNull();
        assertThat(genre.getId()).isEqualTo(1);
    }

    @Test
    @Order(2)
    public void testFindAll_2() {
        Map<Long, Genre> genres = genreStorage.findAll();

        assertThat(genres).isNotNull();
        assertThat(genres.size()).isEqualTo(6);
    }
}
