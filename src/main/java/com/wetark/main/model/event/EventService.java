package com.wetark.main.model.event;

import com.wetark.main.model.BaseService;
import org.springframework.stereotype.Service;

@Service
public class EventService extends BaseService<Event> {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        super(eventRepository);
        this.eventRepository = eventRepository;
    }
}
