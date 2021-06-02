package com.desafiobackend.gestoreventosapi.domain.event.cases;

import com.desafiobackend.gestoreventosapi.domain.event.EventController;
import com.desafiobackend.gestoreventosapi.domain.event.EventDTO;
import com.desafiobackend.gestoreventosapi.domain.event.EventService;
import org.junit.jupiter.api.AfterAll;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class GetEventsCaseController {

    @Mock
    private EventService eventService;

    @InjectMocks
    private static EventController eventController;

    @BeforeTestExecution
    public void setUp() {
        MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @AfterAll
    static void tearDown() {
        eventController = null;
    }



    @Test
    public void whenReturn200OK() {

        when(eventService.getAll(EventDTO.class)).thenReturn(List.of(new EventDTO()));

        ResponseEntity response = eventController.getEvents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenReturn404NotFound() {

        when(eventService.getAll(EventDTO.class)).thenReturn(null);

        ResponseEntity response = eventController.getEvents();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
