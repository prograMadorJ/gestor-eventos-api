package com.desafiobackend.gestoreventosapi.domain.event;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import com.desafiobackend.gestoreventosapi.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EventService extends RestServiceBaseImpl<Event, EventDTO> {


    @Autowired
    private UserService userService;

    @Autowired
    private EventRules eventRules;

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository repository) {
        super(repository);
        this.eventRepository = repository;
    }

    @Override
    public String create(Event event) {
        if(
                eventRules.isNotValid
                        .whenUserNotExists(userService.getOne(event.getUser().getId()))
                        .fullValidate()
        ) return EventResponseMessages.USER_NOT_EXISTS;
        return super.create(event);
    }

    @Override
    public String update(String id, Event event) {
        if(
                eventRules.isNotValid
                        .whenUserNotExists(userService.getOne(event.getUser().getId()))
                        .fullValidate()
        ) return EventResponseMessages.USER_NOT_EXISTS;
        return super.update(id, event);
    }

    public List<Event> getEventsByUserId(String id) {
        List<Event> result = eventRepository.findEventsByUserId(id).orElse(null);
        if(!Objects.equals(result, null)) return result;
        return null;
    }

}
