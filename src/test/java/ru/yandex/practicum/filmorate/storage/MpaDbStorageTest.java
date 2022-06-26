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
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MpaDbStorageTest {
    private final MpaDbStorage mpaStorage;

    @Test
    @Order(1)
    public void testFindOne_1() {
        Mpa mpa = mpaStorage.findOne((long) 1).orElse(null);

        assertThat(mpa).isNotNull();
        assertThat(mpa.getId()).isEqualTo(1);
    }

    @Test
    @Order(2)
    public void testFindAll_2() {
        Map<Long, Mpa> mpaList = mpaStorage.findAll();

        assertThat(mpaList).isNotNull();
        assertThat(mpaList.size()).isEqualTo(5);
    }
}
