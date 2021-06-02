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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class GetOneEventCaseController {

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
        eventController= null;
    }



    @Test
    public void whenReturn200OK() {

        when(eventService.getOne("", EventDTO.class)).thenReturn(new EventDTO());

        ResponseEntity response = eventController.getEvent("");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenReturn404NotFound() {

        when(eventService.getOne(null, EventDTO.class)).thenReturn(null);

        ResponseEntity response = eventController.getEvent(null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
