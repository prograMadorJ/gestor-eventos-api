package com.desafiobackend.gestoreventosapi.domain.user.cases;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import com.desafiobackend.gestoreventosapi.domain.user.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class CreateCaseService {


    @Mock
    private static RestServiceBaseImpl<User, UserDTO> restServiceBase;

    @Mock
    private UserRules.IsNotValid notValid;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private static UserService userService;

    private static User user;

    @BeforeTestExecution
    public void setUp() {
        MockMvcBuilders.standaloneSetup(userService).build();
        userService.restServiceBase = mock(RestServiceBaseImpl.class);
    }

    @BeforeEach
    public void eachSetUp() {
        user = new User(
                "",
                "Create User",
                "createuser@email.com",
                "secret_password",
                Collections.singletonList("USER")
        );
    }

    @AfterAll
    public static void tearDown() {
       userService = null;
       user = null;
    }

    @Test
    public void whenCreateUserWithUserValidDataReturnCreatedMessage() {

        String CREATED = UserResponseMessages.CREATED;

        String rawPassword = user.getPassword();

        when(restServiceBase.create(user)).thenReturn(CREATED);

        String result = userService.create(user);

        assertEquals(CREATED, result);
        assertTrue(new BCryptPasswordEncoder().matches(rawPassword, user.getPassword()));
        assertTrue(user.getRoles().contains("USER"));
    }

    @Test
    public void whenCreateUserWithUserExistsReturnConflictMessage() {

       when(notValid.whenUserExistsByEmail(anyBoolean())).thenReturn(true);
       when(notValid.fullValidate()).thenReturn(true);

        String result = userService.create(user);

        assertEquals(UserResponseMessages.CONFLICT, result);

    }
}
