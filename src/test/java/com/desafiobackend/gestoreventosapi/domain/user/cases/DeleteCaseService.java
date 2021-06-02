package com.desafiobackend.gestoreventosapi.domain.user.cases;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import com.desafiobackend.gestoreventosapi.domain.event.EventService;
import com.desafiobackend.gestoreventosapi.domain.user.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class DeleteCaseService {


    @Mock
    private static RestServiceBaseImpl<User, UserDTO> restServiceBase;


    @Mock
    private UserRules userRules;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRules.IsNotValid notValid;

    @Mock
    private EventService eventService;

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


    private void authentication() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getEmail();
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }
        });
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(restServiceBase.toDTO(user, UserDTO.class)).thenReturn(new UserDTO());

        SecurityContextHolder.setContext(context);
    }

    @Test
    public void whenDeleteUserWithUserValidDataReturnDeletedMessage() {

        authentication();

        String DELETED = UserResponseMessages.DELETED;

        when(restServiceBase.delete(user.getId())).thenReturn(DELETED);

        String result = userService.delete(user.getId());

        assertEquals(DELETED, result);
    }

    @Test
    public void whenDeleteUserReturnCannotDeleteHasEventsMessage() {

        authentication();

        when(notValid.whenUserHasOneOrManyEvent(anyList())).thenReturn(true);
        when(notValid.fullValidate()).thenReturn(true);

        String result = userService.delete(user.getId());

        assertEquals(UserResponseMessages.CANNOT_DELETE_HAS_EVENTS, result);

    }

    @Test
    public void whenDeleteUserReturnCannotDeleteMessage() {

        authentication();

        when(userRepository.findByIdProjectRole(user.getId())).thenReturn(Optional.of(user));
        when(notValid.whenUserAuthWithRoleIsNotSystem(anyString())).thenReturn(true);
        when(notValid.whenUserAuthWithRoleIsSystemTryDeleteYourSelf(new UserDTO().getRoles().toString(), user)).thenReturn(true);
        when(notValid.validateAtLeastOne()).thenReturn(true);

        String result = userService.delete(user.getId());

        assertEquals(UserResponseMessages.CANNOT_DELETE, result);

    }


}
