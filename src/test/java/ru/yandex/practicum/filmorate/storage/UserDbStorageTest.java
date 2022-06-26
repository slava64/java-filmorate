package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(OrderAnnotation.class)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    @Order(1)
    public void testAdd_1() {
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
        assertThat(addedUser.getId()).isEqualTo(user.getId());
        assertThat(addedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(addedUser.getLogin()).isEqualTo(user.getLogin());
        assertThat(addedUser.getName()).isEqualTo(user.getName());
        assertThat(addedUser.getBirthday()).isEqualTo(user.getBirthday());
    }

    @Test
    @Order(2)
    public void testUpdate_2() {
        User user = new User(
                (long) 1,
                "update@test.com",
                "update",
                "Update",
                LocalDate.of(1992, 6, 1)
        );

        userStorage.update(user);

        User updatedUser = userStorage.findOne((long) 1).orElse(null);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(user.getId());
        assertThat(updatedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(updatedUser.getLogin()).isEqualTo(user.getLogin());
        assertThat(updatedUser.getName()).isEqualTo(user.getName());
        assertThat(updatedUser.getBirthday()).isEqualTo(user.getBirthday());
    }

    @Test
    @Order(3)
    public void testFindOne_3() {
        User user = userStorage.findOne((long) 1).orElse(null);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    @Order(4)
    public void testAddFriend_4() {
        User newUser = new User(
                (long) 2,
                "friend@test.com",
                "friend",
                "Friend",
                LocalDate.of(1993, 6, 1)
        );

        userStorage.add(newUser);
        User friend = userStorage.findOne((long) 2).orElse(null);
        assertThat(friend).isNotNull();

        User user = userStorage.findOne((long) 1).orElse(null);
        assertThat(user).isNotNull();

        userStorage.addFriend(user, friend);

        friend = userStorage.findOne((long) 2).orElse(null);
        assertThat(friend).isNotNull();

        user = userStorage.findOne((long) 1).orElse(null);
        assertThat(user).isNotNull();

        assertThat(user.getFriends().size()).isEqualTo(1);
        assertThat(friend.getFriends().size()).isEqualTo(0);
    }

    @Test
    @Order(5)
    public void testDeleteFriend_5() {
        User friend = userStorage.findOne((long) 2).orElse(null);
        assertThat(friend).isNotNull();

        User user = userStorage.findOne((long) 1).orElse(null);
        assertThat(user).isNotNull();

        userStorage.deleteFriend(user, friend);

        assertThat(user.getFriends().size()).isEqualTo(0);
        assertThat(friend.getFriends().size()).isEqualTo(0);
    }

    @Test
    @Order(6)
    public void testFindAll_6() {
        Map<Long, User> users = userStorage.findAll();
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @Order(7)
    public void testDelete_7() {
        User user = userStorage.findOne((long) 2).orElse(null);
        assertThat(user).isNotNull();

        userStorage.delete((long) 2);

        user = userStorage.findOne((long) 2).orElse(null);
        assertThat(user).isNull();
    }
} 