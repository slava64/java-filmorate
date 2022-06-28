package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Review {

    private Long reviewId;
    private String content;
    private Boolean isPositive;
    private Long filmId;
    private Long userId;
    private Integer rate;
}
