package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.Map;

public interface EventStorage {
    // Новое событие
    public void add(Event event);
    // Все записи
    public Map<Long, Event> findAll();
}
