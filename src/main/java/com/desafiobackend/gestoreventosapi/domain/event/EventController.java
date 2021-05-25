package com.desafiobackend.gestoreventosapi.domain.event;

import com.desafiobackend.gestoreventosapi.domain.user.UserService;
import com.desafiobackend.gestoreventosapi.utils.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Api("Eventos")
@RestController
@RequestMapping(value = "/api/events", consumes = "application/json", produces = "application/json")
public class EventController {

    @Autowired
    private EventService service;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Criar Evento", notes = "Cria um novo evento", response = RestResponse.class)
    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        if(userService.getOne(event.getUser().getId()) == null) return new RestResponse("user is not null").status(HttpStatus.BAD_REQUEST);
        String result = service.create(event);
        return result.equals("created")
                ? new RestResponse("create success").status(HttpStatus.OK)
                : new RestResponse("create error").status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Atualizar Evento", notes = "Atualiza um evento", response = RestResponse.class)
    @PutMapping
    public ResponseEntity updateEvent(@RequestBody Event event) {
        if(userService.getOne(event.getUser().getId()) == null)  return new RestResponse("user is not null").status(HttpStatus.BAD_REQUEST);
        String result = service.update(event.getId(), event);
        if (result.equals("not found")) return new RestResponse("event not found").status(HttpStatus.NOT_FOUND);
        if (result.equals("updated")) return new RestResponse("update success").status(HttpStatus.OK);
        return new RestResponse("update error").status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Remover Evento", notes = "Remove um evento", response = RestResponse.class)
    @DeleteMapping("{id}")
    public ResponseEntity deleteEvent(@PathVariable String id) {
        String result = service.delete(id);
        if (result.equals("not found")) return new RestResponse("event not found").status(HttpStatus.NOT_FOUND);
        if (result.equals("deleted")) return new RestResponse("delete success").status(HttpStatus.OK);
        return new RestResponse("delete error").status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Obter Evento", notes = "Obtem um evento pelo id", response = RestResponse.class)
    @GetMapping("{id}")
    public ResponseEntity getEvent(@PathVariable String id) {
        EventDTO result = service.getOne(id, EventDTO.class);
        if (Objects.equals(result, null)) return new RestResponse("event not found").status(HttpStatus.NOT_FOUND);
        return new RestResponse("event found", result).status(HttpStatus.OK);
    }

    @ApiOperation(value = "Obter Eventos", notes = "Obtem todos os eventos", response = RestResponse.class)
    @GetMapping
    public ResponseEntity getEvents() {
        List<EventDTO> result = service.getAll(EventDTO.class);
        if (Objects.equals(result, null)) return new RestResponse("events not found").status(HttpStatus.NOT_FOUND);
        return new RestResponse("events found", result).status(HttpStatus.OK);
    }

    @ApiOperation(value = "Obter Evento por Usuário", notes = "Obtem um evento de um usuário especificado pelo o id de usuário", response = RestResponse.class)
    @GetMapping("/user/{id}")
    public ResponseEntity getEventsByUserId(@PathVariable String id) {
        List<Event> result = service.getEventsByUserId(id);
        if (result.size() == 0) return new RestResponse("events by user id not found", null).status(HttpStatus.NOT_FOUND);
        return new RestResponse("events by user id found", result).status(HttpStatus.OK);
    }
}
