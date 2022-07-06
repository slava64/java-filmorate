package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.dao.EventStorage;

import java.util.Collection;

@Service
public class EventService {
    private final EventStorage eventStorage;

    @Autowired
    public EventService(EventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    public Collection<Event> findAllByUserId(Long id) {
        return eventStorage.findAllByUserId(id).values();
    }
}
