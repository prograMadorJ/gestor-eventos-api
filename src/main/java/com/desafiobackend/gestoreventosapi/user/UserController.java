package com.desafiobackend.gestoreventosapi.user;

import com.desafiobackend.gestoreventosapi.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) {
        service.create(user);
        return new RestResponse("create success").status(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity updateUser(@RequestBody User user) {
        String result = service.update(user.getId(), user);
        if (result.equals("not found")) return new RestResponse("user not found").status(HttpStatus.NOT_FOUND);
        if (result.equals("updated")) return new RestResponse("update success").status(HttpStatus.OK);
        return new RestResponse("update error").status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        String result = service.delete(id);
        if (result.equals("not found")) return new RestResponse("user not found").status(HttpStatus.NOT_FOUND);
        if (result.equals("deleted")) return new RestResponse("delete success").status(HttpStatus.OK);
        return new RestResponse("delete error").status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("{id}")
    public ResponseEntity getUser(@PathVariable String id) {
        UserDTO result = service.getOne(id, UserDTO.class);
        if (Objects.equals(result, null)) return new RestResponse("user not found").status(HttpStatus.NOT_FOUND);
        return new RestResponse("user found", result).status(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getUsers() {
        List<User> result = service.getAll();
        if (Objects.equals(result, null)) return new RestResponse("users not found").status(HttpStatus.NOT_FOUND);
        return new RestResponse("users found", result).status(HttpStatus.OK);
    }
}
