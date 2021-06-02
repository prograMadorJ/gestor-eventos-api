package com.desafiobackend.gestoreventosapi.domain.user.cases;

import com.desafiobackend.gestoreventosapi.domain.user.User;
import com.desafiobackend.gestoreventosapi.domain.user.UserController;
import com.desafiobackend.gestoreventosapi.domain.user.UserResponseMessages;
import com.desafiobackend.gestoreventosapi.domain.user.UserService;
import org.junit.jupiter.api.*;
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
public class UpdateCaseController {

    @Mock
    private UserService userService;

    @InjectMocks
    private static UserController userController;

    private static User user;

    @BeforeAll
    static void init() {
        user = new User(
                "",
                "Update User",
                "updateuser@email.com",
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

        when(userService.update(user.getId(), user)).thenReturn(UserResponseMessages.UPDATED);

        ResponseEntity response = userController.updateUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenReturnGivenNullWithStatus400() {

        when(userService.update(user.getId(), user)).thenReturn(UserResponseMessages.GIVEN_NULL);

        ResponseEntity response = userController.updateUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenReturnIdNullWithStatus400() {

        when(userService.update(user.getId(), user)).thenReturn(UserResponseMessages.ID_NULL);

        ResponseEntity response = userController.updateUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenReturnStatus409Conflict() {

        when(userService.update(user.getId(), user)).thenReturn(UserResponseMessages.CONFLICT);

        ResponseEntity response = userController.updateUser(user);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void whenReturnStatus404NotFound() {

        when(userService.update(user.getId(), user)).thenReturn(UserResponseMessages.NOT_FOUND);

        ResponseEntity response = userController.updateUser(user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenReturnStatus500Error() {

        when(userService.update(user.getId(), user)).thenReturn(UserResponseMessages.UPDATED_ERROR);

        ResponseEntity response = userController.updateUser(user);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
