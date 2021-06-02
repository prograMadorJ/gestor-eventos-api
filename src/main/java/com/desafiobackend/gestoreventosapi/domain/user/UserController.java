package com.desafiobackend.gestoreventosapi.domain.user;

import com.desafiobackend.gestoreventosapi.utils.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Api(value = "Usuário", consumes = "application/json", produces = "application/json")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @ApiOperation(value = "Criar Usuário", notes = "Cria um novo usuário", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @PostMapping
    public ResponseEntity createUser(@Validated @RequestBody User user) {
        UserResponseMessages ur = new UserResponseMessages(service.create(user), service.toDTO(user, UserDTO.class).getId());
        ur.addCaseResponseContent(UserResponseMessages.CREATED, HttpStatus.OK);
        ur.addCaseResponse(UserResponseMessages.GIVEN_NULL, HttpStatus.BAD_REQUEST);
        ur.addCaseResponse(UserResponseMessages.CONFLICT, HttpStatus.CONFLICT);
        return ur.buildResponse();
    }

    @ApiOperation(value = "Atualizar Usuário", notes = "Atualiza um usuário", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @PutMapping
    public ResponseEntity updateUser(@RequestBody User user) {
        UserResponseMessages ur = new UserResponseMessages(service.update(user.getId(), user));
        ur.addCaseResponse(UserResponseMessages.UPDATED, HttpStatus.OK);
        ur.addCaseResponse(UserResponseMessages.GIVEN_NULL, HttpStatus.BAD_REQUEST);
        ur.addCaseResponse(UserResponseMessages.ID_NULL, HttpStatus.BAD_REQUEST);
        ur.addCaseResponse(UserResponseMessages.CONFLICT, HttpStatus.CONFLICT);
        ur.addCaseResponse(UserResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND);
        ur.addCaseResponse(UserResponseMessages.UPDATED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        return ur.buildResponse();
    }

    @ApiOperation(value = "Remover Usuário", notes = "Remove um usuário", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @DeleteMapping("{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        UserResponseMessages ur = new UserResponseMessages(service.delete(id));
        ur.addCaseResponse(UserResponseMessages.DELETED, HttpStatus.OK);
        ur.addCaseResponse(UserResponseMessages.ID_NULL, HttpStatus.BAD_REQUEST);
        ur.addCaseResponse(UserResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND);
        ur.addCaseResponse(UserResponseMessages.CANNOT_DELETE_HAS_EVENTS, HttpStatus.UNPROCESSABLE_ENTITY);
        ur.addCaseResponse(UserResponseMessages.CANNOT_DELETE, HttpStatus.UNPROCESSABLE_ENTITY);
        ur.addCaseResponse(UserResponseMessages.DELETE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        return ur.buildResponse();
    }

    @ApiOperation(value = "Obter um Usuário", notes = "Obtem um usuário pelo id", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @GetMapping("{id}")
    public ResponseEntity getUser(@PathVariable String id) {
        UserResponseMessages ur = new UserResponseMessages(UserResponseMessages.FOUND, service.getOne(id, UserDTO.class));
        ur.addCaseResponseContent(UserResponseMessages.FOUND, HttpStatus.OK);
        ur.addCaseResponseContentNull(UserResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND);
        return ur.buildResponse();
    }

    @ApiOperation(value = "Obter Todos Usuários", notes = "Obtem todos os usuários", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @GetMapping
    public ResponseEntity getUsers() {
        UserResponseMessages ur = new UserResponseMessages(UserResponseMessages.FOUND, service.getAll(UserDTO.class));
        ur.addCaseResponseContentNull(UserResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND);
        ur.addCaseResponseContent(UserResponseMessages.FOUND, HttpStatus.OK);
        return ur.buildResponse();
    }
}
