package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Event {
    public enum EventType {
        LIKE, REVIEW, FRIEND
    }

    public enum EventOperation {
        REMOVE, ADD, UPDATE
    }

    public Event(
            Long eventId,
            Long userId,
            Long entityId,
            EventType eventType,
            EventOperation operation,
            Long timestamp
    ) {
        this.eventId = eventId;
        this.userId = userId;
        this.entityId = entityId;
        this.eventType = eventType;
        this.operation = operation;
        this.timestamp = timestamp;
    }

    private Long eventId;

    @NotNull
    private Long userId;

    @NotNull
    private Long entityId;

    @NotNull
    private EventType eventType;

    @NotNull
    private EventOperation operation;

    @NotNull
    private Long timestamp;
}
