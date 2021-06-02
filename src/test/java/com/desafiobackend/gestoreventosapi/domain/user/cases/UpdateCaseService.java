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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class UpdateCaseService {


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
    public void whenUpdateUserWithUserValidDataReturnUpdatedMessage() {

        authentication();

        String UPDATED = UserResponseMessages.UPDATED;

        String rawPassword = user.getPassword();

        when(restServiceBase.update(user.getId(), user)).thenReturn(UPDATED);

        String result = userService.update(user.getId(), user);

        assertEquals(UPDATED, result);
        assertTrue(new BCryptPasswordEncoder().matches(rawPassword, user.getPassword()));
    }

    @Test
    public void whenUpdateUserWithUserAuthenticateReturnConflictMessage() {

        authentication();

        when(notValid.whenUserAuthIsAdmin(anyBoolean())).thenReturn(true);
        when(notValid.whenUserAuthByEmailSameAsUser(new UserDTO().getEmail(), user.getEmail())).thenReturn(true);
        when(notValid.fullValidate()).thenReturn(true);

        String result = userService.update(user.getId(), user);

        assertEquals(UserResponseMessages.CONFLICT, result);

    }

    @Test
    public void whenUpdateUserWithUserNotAuthenticateReturnConflictMessage() {

        authentication();

        when(notValid.whenUserAuthIsNotAdmin(anyBoolean())).thenReturn(true);
        when(notValid.whenUserAuthByEmailNotSameAsUser(new UserDTO().getEmail(), user.getEmail())).thenReturn(true);
        when(notValid.whenUserExistsByEmail(anyBoolean())).thenReturn(true);
        when(notValid.fullValidate()).thenReturn(true);

        String result = userService.update(user.getId(), user);

        assertEquals(UserResponseMessages.CONFLICT, result);

    }
}
