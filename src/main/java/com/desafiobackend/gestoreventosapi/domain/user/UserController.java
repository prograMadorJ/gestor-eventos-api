package com.desafiobackend.gestoreventosapi.domain.user;

import com.desafiobackend.gestoreventosapi.utils.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Api("Usuário")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @ApiOperation("Cria um novo usuário")
    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) {
        String result = service.create(user);
        return result.equals("conflict")
                ? new RestResponse("user conflict").status(HttpStatus.CONFLICT)
                : new RestResponse("create success").status(HttpStatus.OK);
    }

    @ApiOperation("Atualiza um usuário")
    @PutMapping
    public ResponseEntity updateUser(@RequestBody User user) {
        String result = service.update(user.getId(), user);
        if (result.equals("not found")) return new RestResponse("user not found").status(HttpStatus.NOT_FOUND);
        if (result.equals("updated")) return new RestResponse("update success").status(HttpStatus.OK);
        if (result.equals("conflict")) return new RestResponse("update conflict").status(HttpStatus.CONFLICT);
        return new RestResponse("update error").status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation("Remove um usuário")
    @DeleteMapping("{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        String result = service.delete(id);
        if (result.equals("not found")) return new RestResponse("user not found").status(HttpStatus.NOT_FOUND);
        if (result.equals("deleted")) return new RestResponse("delete success").status(HttpStatus.OK);
        return new RestResponse("delete error").status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation("Obtem um usuário pelo id")
    @GetMapping("{id}")
    public ResponseEntity getUser(@PathVariable String id) {
        UserDTO result = service.getOne(id, UserDTO.class);
        if (Objects.equals(result, null)) return new RestResponse("user not found").status(HttpStatus.NOT_FOUND);
        return new RestResponse("user found", result).status(HttpStatus.OK);
    }

    @ApiOperation("Obtem todos os usuários")
    @GetMapping
    public ResponseEntity getUsers() {
        List<UserDTO> result = service.getAll(UserDTO.class);
        if (Objects.equals(result, null)) return new RestResponse("users not found").status(HttpStatus.NOT_FOUND);
        return new RestResponse("users found", result).status(HttpStatus.OK);
    }
}
