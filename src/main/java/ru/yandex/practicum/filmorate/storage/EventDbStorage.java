package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

@Component
@Primary
@Slf4j
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;
    private static ApplicationContext applicationContext;
    String ADD_EVENT_SQL = "insert into events(user_id, entity_id, event_type, operation, timestamp) " +
            "values (?, ?, ?, ?, ?)";
    String FIND_ADD_BY_USER_ID_SQL = "select * from events where user_id = ?";

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        EventDbStorage.applicationContext = applicationContext;
    }

    @Autowired
    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Добавляет новое событие
    public static void addEvent(
            Long userId,
            Long entityId,
            Event.EventType eventType,
            Event.EventOperation operation
    ) {
        Instant currentInstant = Instant.now();

        log.debug(
                "{}: Пользователь {} {} {} № {}",
                currentInstant,
                userId,
                operation.toString(),
                eventType.toString(),
                entityId
        );

        applicationContext.getBean(EventDbStorage.class).add(
                new Event(
                        null,
                        userId,
                        entityId,
                        eventType,
                        operation,
                        currentInstant.toEpochMilli()
                )
        );
    }

    public void add(Event event) {
        jdbcTemplate.update(
                ADD_EVENT_SQL,
                event.getUserId(),
                event.getEntityId(),
                event.getEventType().name(),
                event.getOperation().name(),
                event.getTimestamp()
        );
    }

    @Override
    public Map<Long, Event> findAllByUserId(Long id) {
        return jdbcTemplate.query(FIND_ADD_BY_USER_ID_SQL, (ResultSet rs) -> {
            Map <Long, Event> results = new HashMap<>();
            while (rs.next()) {
                results.put(rs.getLong("event_id"), mapRow(rs, rs.getRow()));
            }
            return results;
        }, id);
    }

    private Event mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Event(
                resultSet.getLong("event_id"),
                resultSet.getLong("user_id"),
                resultSet.getLong("entity_id"),
                Event.EventType.valueOf(resultSet.getString("event_type")),
                Event.EventOperation.valueOf(resultSet.getString("operation")),
                resultSet.getLong("timestamp")
        );
    }
}
