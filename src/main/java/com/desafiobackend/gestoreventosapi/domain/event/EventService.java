package com.desafiobackend.gestoreventosapi.domain.event;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EventService extends RestServiceBaseImpl<Event, EventDTO> {

    @Autowired
    public EventService(EventRepository repository) {
        super(repository);
    }

    public List<Event> getEventsByUserId(String id) {
        List<Event> result = ((EventRepository) repository).findEventsByUserId(id).orElse(null);
        if(!Objects.equals(result, null)) return result;
        return null;
    }

}
