package com.desafiobackend.gestoreventosapi.domain.user.cases;

import com.desafiobackend.gestoreventosapi.domain.user.UserController;
import com.desafiobackend.gestoreventosapi.domain.user.UserDTO;
import com.desafiobackend.gestoreventosapi.domain.user.UserService;
import org.junit.jupiter.api.AfterAll;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class GetOneUserCaseController {

    @Mock
    private UserService userService;

    @InjectMocks
    private static UserController userController;

    @BeforeTestExecution
    public void setUp() {
        MockMvcBuilders.standaloneSetup(userController).build();
    }

    @AfterAll
    static void tearDown() {
        userController = null;
    }



    @Test
    public void whenReturn200OK() {

        when(userService.getOne("", UserDTO.class)).thenReturn(new UserDTO());

        ResponseEntity response = userController.getUser("");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenReturn404NotFound() {

        when(userService.getOne(null, UserDTO.class)).thenReturn(null);

        ResponseEntity response = userController.getUser(null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
