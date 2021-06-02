package com.desafiobackend.gestoreventosapi.domain.event.cases;

import com.desafiobackend.gestoreventosapi.domain.event.Event;
import com.desafiobackend.gestoreventosapi.domain.event.EventController;
import com.desafiobackend.gestoreventosapi.domain.event.EventResponseMessages;
import com.desafiobackend.gestoreventosapi.domain.event.EventService;
import com.desafiobackend.gestoreventosapi.domain.user.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class CreateCaseController {

    @Mock
    private EventService eventService;

    @InjectMocks
    private static EventController eventController;

    private static User user;

    private static Event event;


    @BeforeAll
    public static void init() {
        user = new User(
                "anyId",
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

    @BeforeTestExecution
    public void setUp() {
        MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @AfterAll
    static void tearDown() {
        eventController = null;
        user = null;
        event = null;
    }

    @Test
    public void whenReturnStatus200OK() {

        event.setId("newId");

        when(eventService.create(event)).thenReturn(EventResponseMessages.CREATED);

        ResponseEntity response = eventController.createEvent(event);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenReturnStatus422UnprocessableEntity() {

        when(eventService.create(event)).thenReturn(EventResponseMessages.USER_NOT_EXISTS);

        ResponseEntity response = eventController.createEvent(event);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());

    }

    @Test
    public void whenReturnStatus500Error() {

        when(eventService.create(event)).thenReturn(EventResponseMessages.CREATE_ERROR);

        ResponseEntity response = eventController.createEvent(event);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
