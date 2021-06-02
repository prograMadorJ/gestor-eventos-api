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
        EventResponseMessages ur = new EventResponseMessages(service.create(event), event.getId());
        ur.addCaseResponseContent(EventResponseMessages.CREATED, HttpStatus.OK);
        ur.addCaseResponse(EventResponseMessages.USER_NOT_EXISTS, HttpStatus.UNPROCESSABLE_ENTITY);
        ur.addCaseResponse(EventResponseMessages.CREATE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        return ur.buildResponse();
    }

    @ApiOperation(value = "Atualizar Evento", notes = "Atualiza um evento", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @PutMapping
    public ResponseEntity updateEvent(@RequestBody Event event) {
        EventResponseMessages ur = new EventResponseMessages(service.update(event.getId(), event));
        ur.addCaseResponse(EventResponseMessages.UPDATED, HttpStatus.OK);
        ur.addCaseResponse(EventResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND);
        ur.addCaseResponse(EventResponseMessages.USER_NOT_EXISTS, HttpStatus.UNPROCESSABLE_ENTITY);
        ur.addCaseResponse(EventResponseMessages.UPDATED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        return ur.buildResponse();
    }

    @ApiOperation(value = "Remover Evento", notes = "Remove um evento", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @DeleteMapping("{id}")
    public ResponseEntity deleteEvent(@PathVariable String id) {
        EventResponseMessages ur =  new EventResponseMessages(service.delete(id));
        ur.addCaseResponse(EventResponseMessages.DELETED, HttpStatus.OK);
        ur.addCaseResponse(EventResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND);
        ur.addCaseResponse(EventResponseMessages.DELETE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        return ur.buildResponse();
    }

    @ApiOperation(value = "Obter Evento", notes = "Obtem um evento pelo id", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @GetMapping("{id}")
    public ResponseEntity getEvent(@PathVariable String id) {
        EventResponseMessages ur = new EventResponseMessages(EventResponseMessages.FOUND, service.getOne(id, EventDTO.class));
        ur.addCaseResponseContent(EventResponseMessages.FOUND, HttpStatus.OK);
        ur.addCaseResponseContentNull(EventResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND);
        return ur.buildResponse();
    }

    @ApiOperation(value = "Obter Eventos", notes = "Obtem todos os eventos", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @GetMapping
    public ResponseEntity getEvents() {
        EventResponseMessages ur = new EventResponseMessages(EventResponseMessages.FOUND, service.getAll(EventDTO.class));
        ur.addCaseResponseContentNull(EventResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND);
        ur.addCaseResponseContent(EventResponseMessages.FOUND, HttpStatus.OK);
        return ur.buildResponse();
    }

    @ApiOperation(value = "Obter Evento por Usuário", notes = "Obtem um evento de um usuário especificado pelo o id de usuário", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @GetMapping("/user/{id}")
    public ResponseEntity getEventsByUserId(@PathVariable String id) {
        List<Event> result = service.getEventsByUserId(id);
        EventResponseMessages ur = new EventResponseMessages(EventResponseMessages.FOUND, result.size() == 0 ? null : result);
        ur.addCaseResponseContentNull(EventResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND);
        ur.addCaseResponseContent(EventResponseMessages.FOUND, HttpStatus.OK);
        return ur.buildResponse();
    }
}
