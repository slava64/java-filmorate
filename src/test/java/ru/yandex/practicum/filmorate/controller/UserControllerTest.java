package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void isOk() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\": \"test@mail.com\"," +
                                "\"login\": \"test\", \"name\": \"Test\", " +
                                "\"birthday\": \"2022-05-12\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void emptyEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\": \"\"," +
                                "\"login\": \"test\", \"name\": \"Test\", " +
                                "\"birthday\": \"2022-05-12\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void validEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\": \"testmail.com\"," +
                                "\"login\": \"test\", \"name\": \"Test\", " +
                                "\"birthday\": \"2022-05-12\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void emptyLogin() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\": \"test@mail.com\"," +
                                "\"login\": \"\", \"name\": \"Test\", " +
                                "\"birthday\": \"2022-05-12\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void spaceLogin() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\": \"test@mail.com\"," +
                                "\"login\": \"   \", \"name\": \"Test\", " +
                                "\"birthday\": \"2022-05-12\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void emptyName() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\": \"test@mail.com\"," +
                                "\"login\": \"test\", \"name\": \"\", " +
                                "\"birthday\": \"2022-05-12\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void pastBirthday() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\": \"test@mail.com\"," +
                                "\"login\": \"test\", \"name\": \"\", " +
                                "\"birthday\": \"2080-05-12\"}"))
                .andExpect(status().is4xxClientError());
    }
}