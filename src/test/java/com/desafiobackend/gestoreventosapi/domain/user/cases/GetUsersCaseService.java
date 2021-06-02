package com.desafiobackend.gestoreventosapi.domain.user.cases;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import com.desafiobackend.gestoreventosapi.domain.user.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class GetUsersCaseService {


    @Mock
    private static RestServiceBaseImpl<User, UserDTO> restServiceBase;

    @Mock
    private UserRules.IsNotValid notValid;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private static UserService userService;


    @BeforeTestExecution
    public void setUp() {
        MockMvcBuilders.standaloneSetup(userService).build();
        userService.restServiceBase = mock(RestServiceBaseImpl.class);
    }


    @AfterAll
    public static void tearDown() {
       userService = null;
    }

    @Test
    public void whenGetAllReturnListOfUsers() {

        List<UserDTO> userDTOList = Collections.singletonList(new UserDTO());

        when(restServiceBase.getAll(UserDTO.class)).thenReturn(userDTOList);

        List<UserDTO> result = userService.getAll(UserDTO.class);

        assertEquals(userDTOList, result);
    }

    @Test
    public void whenGetAllReturnNull() {

        List<UserDTO> userDTOList = null;

        when(restServiceBase.getAll(UserDTO.class)).thenReturn(userDTOList);

        List<UserDTO> result = userService.getAll(UserDTO.class);

        assertEquals(userDTOList, result);
    }

}
