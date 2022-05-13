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
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void isOk() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Последний герой\"," +
                                "\"description\": \"Описание\", \"releaseDate\": \"1895-12-29\", " +
                                "\"duration\": 100}"))
                .andExpect(status().isOk());
    }

    @Test
    public void emptyName() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"\"," +
                                "\"description\": \"Описание\", \"releaseDate\": \"1895-12-29\", " +
                                "\"duration\": 100}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void longDescription() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Последний герой\"," +
                                "\"description\": \"Слишком длинное описание слишком длинное описание" +
                                "Слишком длинное описание слишком длинное описание " +
                                "Слишком длинное описание слишком длинное описание" +
                                "Слишком длинное описание слишком длинное описание" +
                                "Слишком длинное описание слишком длинное описание" +
                                "\", \"releaseDate\": \"1895-12-29\", " +
                                "\"duration\": 100}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void noValidateReleaseDate() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Последний герой\"," +
                                "\"description\": \"Описание\", \"releaseDate\": \"1895-12-27\", " +
                                "\"duration\": 100}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void negativeDuration() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Последний герой\"," +
                                "\"description\": \"Описание\", \"releaseDate\": \"1895-12-29\", " +
                                "\"duration\": -1}"))
                .andExpect(status().is4xxClientError());
    }
}