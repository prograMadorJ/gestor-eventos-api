package com.desafiobackend.gestoreventosapi.event;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import com.desafiobackend.gestoreventosapi.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService extends RestServiceBaseImpl<Event, EventDTO> {

    @Autowired
    private UserService userService;

    @Autowired
    public EventService(EventRepository repository) {
        super(repository);
    }

    @Override
    public void create(Event event) {
        event.setUser(userService.getOne(event.getUser().getId()));
        super.create(event);
    }

    @Override
    public String update(String id, Event event) {
        event.setUser(userService.getOne(event.getUser().getId()));
        return super.update(id, event);
    }
}
