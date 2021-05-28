package com.desafiobackend.gestoreventosapi.domain.event;

import com.desafiobackend.gestoreventosapi.utils.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Eventos", consumes = "application/json", produces = "application/json")
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService service;

    @ApiOperation(value = "Criar Evento", notes = "Cria um novo evento", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        return new EventResponseMessages()
                .resultMessage(service.create(event))
                .addCaseResponse(EventResponseMessages.USER_NOT_EXISTS, HttpStatus.UNPROCESSABLE_ENTITY)
                .addCaseResponse(EventResponseMessages.CREATED, HttpStatus.OK)
                .addCaseResponse(EventResponseMessages.CREATE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
                .buildResponse();
    }

    @ApiOperation(value = "Atualizar Evento", notes = "Atualiza um evento", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @PutMapping
    public ResponseEntity updateEvent(@RequestBody Event event) {
        return new EventResponseMessages()
                .resultMessage(service.update(event.getId(), event))
                .addCaseResponse(EventResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND)
                .addCaseResponse(EventResponseMessages.USER_NOT_EXISTS, HttpStatus.UNPROCESSABLE_ENTITY)
                .addCaseResponse(EventResponseMessages.UPDATED, HttpStatus.OK)
                .addCaseResponse(EventResponseMessages.UPDATED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
                .buildResponse();
    }

    @ApiOperation(value = "Remover Evento", notes = "Remove um evento", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @DeleteMapping("{id}")
    public ResponseEntity deleteEvent(@PathVariable String id) {
        return new EventResponseMessages()
                .resultMessage(service.delete(id))
                .addCaseResponse(EventResponseMessages.DELETED, HttpStatus.OK)
                .addCaseResponse(EventResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND)
                .addCaseResponse(EventResponseMessages.DELETE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
                .buildResponse();
    }

    @ApiOperation(value = "Obter Evento", notes = "Obtem um evento pelo id", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @GetMapping("{id}")
    public ResponseEntity getEvent(@PathVariable String id) {
        return new EventResponseMessages()
                .resultContent(service.getOne(id, EventDTO.class))
                .resultMessage(EventResponseMessages.FOUND)
                .addCaseResponseContentNull(EventResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND)
                .addCaseResponseContent(EventResponseMessages.FOUND, HttpStatus.OK)
                .buildResponse();
    }

    @ApiOperation(value = "Obter Eventos", notes = "Obtem todos os eventos", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @GetMapping
    public ResponseEntity getEvents() {
        return new EventResponseMessages()
                .resultContent(service.getAll(EventDTO.class))
                .resultMessage(EventResponseMessages.FOUND)
                .addCaseResponseContentNull(EventResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND)
                .addCaseResponseContent(EventResponseMessages.FOUND, HttpStatus.OK)
                .buildResponse();
    }

    @ApiOperation(value = "Obter Evento por Usuário", notes = "Obtem um evento de um usuário especificado pelo o id de usuário", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @GetMapping("/user/{id}")
    public ResponseEntity getEventsByUserId(@PathVariable String id) {
        List<Event> result = service.getEventsByUserId(id);
        return new EventResponseMessages()
                .resultContent(result.size() == 0 ? null : result)
                .resultMessage(EventResponseMessages.FOUND)
                .addCaseResponseContentNull(EventResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND)
                .addCaseResponseContent(EventResponseMessages.FOUND, HttpStatus.OK)
                .buildResponse();
    }
}
