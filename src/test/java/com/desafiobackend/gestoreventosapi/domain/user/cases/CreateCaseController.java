package com.desafiobackend.gestoreventosapi.domain.user.cases;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class CreateCaseController {

    @Mock
    private UserService userService;

    @InjectMocks
    private static UserController userController;

    private static User user;


    @BeforeAll
    public static void init() {
        user = new User(
                "",
                "Create User",
                "createuser@email.com",
                "secret_password",
                Collections.singletonList("USER")
        );
    }

    @BeforeTestExecution
    public void setUp() {
        MockMvcBuilders.standaloneSetup(userController).build();
    }

    @AfterAll
    static void tearDown() {
        userController = null;
        user = null;
    }

    @Test
    public void whenReturnStatus200OK() {

        UserDTO userDTO = new UserDTO();
        userDTO.setId("anyId");

        when(userService.create(user)).thenReturn(UserResponseMessages.CREATED);
        when(userService.toDTO(user, UserDTO.class)).thenReturn(userDTO);

        ResponseEntity response = userController.createUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenReturnStatus400BadRequest() {
        user = new User();

        when(userService.create(user)).thenReturn(UserResponseMessages.GIVEN_NULL);
        when(userService.toDTO(user, UserDTO.class)).thenReturn(new UserDTO());

        ResponseEntity response = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void whenReturnStatus409Conflict() {

        when(userService.create(user)).thenReturn(UserResponseMessages.CONFLICT);
        when(userService.toDTO(user, UserDTO.class)).thenReturn(new UserDTO());

        ResponseEntity response = userController.createUser(user);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}
