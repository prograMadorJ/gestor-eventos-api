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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class GetOneCaseService {


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
    public void whenGetOneReturnOneUser() {

        UserDTO userDTO = new UserDTO();

        when(restServiceBase.getOne("", UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.getOne("", UserDTO.class);

        assertEquals(userDTO, result);
    }

    @Test
    public void whenGetOneWithIdNullReturnNull() {

        UserDTO userDTO = null;

        when(restServiceBase.getOne(null, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.getOne(null, UserDTO.class);

        assertEquals(userDTO, result);
    }

}
