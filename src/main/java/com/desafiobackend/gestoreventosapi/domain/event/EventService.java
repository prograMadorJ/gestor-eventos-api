package com.desafiobackend.gestoreventosapi.domain.event;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import com.desafiobackend.gestoreventosapi.base.RestServiceBaseInterface;
import com.desafiobackend.gestoreventosapi.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EventService implements RestServiceBaseInterface<Event, EventDTO> {

    @Autowired
    private UserService userService;

    @Autowired
    private EventRules.IsNotValid notValid;

    @Autowired
    private EventRepository eventRepository;

    public RestServiceBaseImpl<Event, EventDTO> restServiceBase;

    @Autowired
    private void init() {
        restServiceBase = new RestServiceBaseImpl<>(eventRepository);
    }

    @Override
    public String create(Event event) {
        if(
                notValid.whenUserNotExists(userService.getOne(event.getUser().getId())) &&
                        notValid.fullValidate()
        ) return EventResponseMessages.USER_NOT_EXISTS;
        return restServiceBase.create(event);
    }

    @Override
    public String update(String id, Event event) {
        if(
                notValid.whenUserNotExists(userService.getOne(event.getUser().getId())) &&
                        notValid.fullValidate()
        ) return EventResponseMessages.USER_NOT_EXISTS;
        return restServiceBase.update(id, event);
    }

    @Override
    public String delete(String id) {
        return restServiceBase.delete(id);
    }

    @Override
    public Event getOne(String id) {
        return restServiceBase.getOne(id);
    }

    @Override
    public EventDTO getOne(String id, Class<? extends EventDTO> clazz) {
        return restServiceBase.getOne(id, clazz);
    }

    @Override
    public List<Event> getAll() {
        return restServiceBase.getAll();
    }

    @Override
    public List<EventDTO> getAll(Class<? extends EventDTO> clazz) {
        return restServiceBase.getAll(clazz);
    }

    @Override
    public EventDTO toDTO(Event event, Class<? extends EventDTO> clazz) {
        return restServiceBase.toDTO(event, clazz);
    }

    @Override
    public Event toEntity(EventDTO eventDTO, Class<? extends Event> clazz) {
        return restServiceBase.toEntity(eventDTO, clazz);
    }

    public List<Event> getEventsByUserId(String id) {
        List<Event> result = eventRepository.findEventsByUserId(id).orElse(null);
        if(!Objects.equals(result, null)) return result;
        return null;
    }

}
