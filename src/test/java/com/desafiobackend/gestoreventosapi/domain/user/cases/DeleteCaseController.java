package com.desafiobackend.gestoreventosapi.domain.user.cases;

import com.desafiobackend.gestoreventosapi.domain.user.User;
import com.desafiobackend.gestoreventosapi.domain.user.UserController;
import com.desafiobackend.gestoreventosapi.domain.user.UserResponseMessages;
import com.desafiobackend.gestoreventosapi.domain.user.UserService;
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
public class DeleteCaseController {

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

        when(userService.delete(user.getId())).thenReturn(UserResponseMessages.DELETED);

        ResponseEntity response = userController.deleteUser(user.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenReturnIdNullWithStatus400() {

        when(userService.delete(user.getId())).thenReturn(UserResponseMessages.ID_NULL);

        ResponseEntity response = userController.deleteUser(user.getId());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenReturnStatus404NotFound() {

        when(userService.delete(user.getId())).thenReturn(UserResponseMessages.NOT_FOUND);

        ResponseEntity response = userController.deleteUser(user.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void whenWithCannotDeleteHasEventsMessageReturnStatus422UnprocessableEntity() {

        when(userService.delete(user.getId())).thenReturn(UserResponseMessages.CANNOT_DELETE_HAS_EVENTS);

        ResponseEntity response = userController.deleteUser(user.getId());

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void whenWithCannotDeleteMessageReturnStatus422UnprocessableEntity() {

        when(userService.delete(user.getId())).thenReturn(UserResponseMessages.CANNOT_DELETE);

        ResponseEntity response = userController.deleteUser(user.getId());

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }


    @Test
    public void whenReturnStatus500Error() {

        when(userService.delete(user.getId())).thenReturn(UserResponseMessages.DELETE_ERROR);

        ResponseEntity response = userController.deleteUser(user.getId());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


}
