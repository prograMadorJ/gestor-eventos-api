package com.desafiobackend.gestoreventosapi.domain.user;

import com.desafiobackend.gestoreventosapi.utils.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity createUser(@RequestBody User user) {
        return new UserResponseMessages()
                .resultMessage(service.create(user))
                .addCaseResponse(UserResponseMessages.CONFLICT, HttpStatus.CONFLICT)
                .addCaseResponse(UserResponseMessages.CREATED, HttpStatus.OK)
                .buildResponse();
    }

    @ApiOperation(value = "Atualizar Usuário", notes = "Atualiza um usuário", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @PutMapping
    public ResponseEntity updateUser(@RequestBody User user) {
        return new UserResponseMessages()
                .resultMessage(service.update(user.getId(), user))
                .addCaseResponse(UserResponseMessages.UPDATED, HttpStatus.OK)
                .addCaseResponse(UserResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND)
                .addCaseResponse(UserResponseMessages.CONFLICT, HttpStatus.CONFLICT)
                .addCaseResponse(UserResponseMessages.UPDATED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
                .buildResponse();
    }

    @ApiOperation(value = "Remover Usuário", notes = "Remove um usuário", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @DeleteMapping("{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        return new UserResponseMessages()
                .resultMessage(service.delete(id))
                .addCaseResponse(UserResponseMessages.DELETED, HttpStatus.OK)
                .addCaseResponse(UserResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND)
                .addCaseResponse(UserResponseMessages.CANNOT_DELETE_HAS_EVENTS, HttpStatus.UNPROCESSABLE_ENTITY)
                .addCaseResponse(UserResponseMessages.CANNOT_DELETE, HttpStatus.UNPROCESSABLE_ENTITY)
                .addCaseResponse(UserResponseMessages.DELETE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
                .buildResponse();
    }

    @ApiOperation(value = "Obter um Usuário", notes = "Obtem um usuário pelo id", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @GetMapping("{id}")
    public ResponseEntity getUser(@PathVariable String id) {
        return new UserResponseMessages()
                .resultContent(service.getOne(id, UserDTO.class))
                .resultMessage(UserResponseMessages.FOUND)
                .addCaseResponseContentNull(UserResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND)
                .addCaseResponseContent(UserResponseMessages.FOUND, HttpStatus.OK)
                .buildResponse();
    }

    @ApiOperation(value = "Obter Todos Usuários", notes = "Obtem todos os usuários", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @GetMapping
    public ResponseEntity getUsers() {
        return new UserResponseMessages()
                .resultContent(service.getAll(UserDTO.class))
                .resultMessage(UserResponseMessages.FOUND)
                .addCaseResponseContentNull(UserResponseMessages.NOT_FOUND, HttpStatus.NOT_FOUND)
                .addCaseResponseContent(UserResponseMessages.FOUND, HttpStatus.OK)
                .buildResponse();
    }
}
