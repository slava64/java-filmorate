package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FriendDbStorageTest {
    private final UserDbStorage userStorage;
    private final FriendDbStorage friendStorage;

    @Test
    @Order(1)
    public void testAddFriend_1() {
        User user1 = new User(
                (long) 1,
                "test@test.com",
                "test",
                "Name",
                LocalDate.of(1991, 6, 1)
        );
        userStorage.add(user1);
        User user = userStorage.findOne((long) 1).orElse(null);
        assertThat(user).isNotNull();

        User user2 = new User(
                (long) 2,
                "friend@test.com",
                "friend",
                "Friend",
                LocalDate.of(1993, 6, 1)
        );
        userStorage.add(user2);
        User friend = userStorage.findOne((long) 2).orElse(null);
        assertThat(friend).isNotNull();

        friendStorage.add(user, friend);

        user = userStorage.findOne((long) 1).orElse(null);
        assertThat(user).isNotNull();

        friend = userStorage.findOne((long) 2).orElse(null);
        assertThat(friend).isNotNull();

        assertThat(user.getFriends().size()).isEqualTo(1);
        assertThat(friend.getFriends().size()).isEqualTo(0);
    }

    @Test
    @Order(2)
    public void testDeleteFriend_2() {
        User friend = userStorage.findOne((long) 2).orElse(null);
        assertThat(friend).isNotNull();

        User user = userStorage.findOne((long) 1).orElse(null);
        assertThat(user).isNotNull();

        friendStorage.delete(user, friend);

        assertThat(user.getFriends().size()).isEqualTo(0);
        assertThat(friend.getFriends().size()).isEqualTo(0);
    }
}
