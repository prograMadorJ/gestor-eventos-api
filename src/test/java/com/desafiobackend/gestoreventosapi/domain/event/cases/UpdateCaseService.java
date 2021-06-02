package com.desafiobackend.gestoreventosapi.domain.event.cases;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import com.desafiobackend.gestoreventosapi.domain.event.*;
import com.desafiobackend.gestoreventosapi.domain.user.User;
import com.desafiobackend.gestoreventosapi.domain.user.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class UpdateCaseService {


    @Mock
    private static RestServiceBaseImpl<Event, EventDTO> restServiceBase;

    @Mock
    private static UserService userService;

    @Mock
    private EventRules.IsNotValid notValid;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private static EventService eventService;

    private static User user;

    private static Event event;

    @BeforeTestExecution
    public void setUp() {
        MockMvcBuilders.standaloneSetup(eventService).build();
        eventService.restServiceBase = mock(RestServiceBaseImpl.class);
    }

    @BeforeEach
    public void eachSetUp() {
        user = new User(
                "",
                "Create User",
                "createuser@email.com",
                "secret_password",
                Collections.singletonList("USER")
        );

        event = new Event(
                null,
                "New Event",
                new Date(),
                user
        );
    }

    @AfterAll
    public static void tearDown() {
        eventService = null;
        user = null;
    }

    @Test
    public void whenUpdateEventWithEventValidDataReturnUpdatedMessage() {

        String UPDATED = EventResponseMessages.UPDATED;

        when(restServiceBase.update(event.getId(), event)).thenReturn(UPDATED);

        String result = eventService.update(event.getId(), event);

        assertEquals(UPDATED, result);
    }


    @Test
    public void whenUpdateEventWithUserNotExistsReturnUserNotExistsMessage() {

        when(userService.getOne(user.getId())).thenReturn(null);
        when(notValid.whenUserNotExists(userService.getOne(user.getId()))).thenReturn(true);
        when(notValid.fullValidate()).thenReturn(true);

        String result = eventService.update(event.getId(), event);

        assertEquals(EventResponseMessages.USER_NOT_EXISTS, result);

    }
}
