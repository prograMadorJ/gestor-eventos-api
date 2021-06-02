package com.desafiobackend.gestoreventosapi.config.security.auth;

import com.desafiobackend.gestoreventosapi.config.security.service.UserDetailsServiceImpl;
import com.desafiobackend.gestoreventosapi.domain.user.User;
import com.desafiobackend.gestoreventosapi.domain.user.UserDTO;
import com.desafiobackend.gestoreventosapi.domain.user.UserSeed;
import com.desafiobackend.gestoreventosapi.domain.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@EnabledIf(
        expression = "${tests.mvc.enabled}",
        reason = "MVC test only at local development",
        loadContext = true)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationCaseAPI {


    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserSeed userSeed;

    @Autowired
    private UserService userServiceHelp;

    @Autowired
    private MockMvc mockMvc;

    private User user;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        user = userSeed.getData();
        userDTO = userServiceHelp.toDTO(userSeed.getData(), UserDTO.class);
    }

    @Test
    public void whenAuthenticateUserWithSuccess() throws Exception {
        Map<String, String> json = new HashMap<>();
        json.put("username", user.getEmail());
        json.put("password", user.getPassword());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(any(Authentication.class));
        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList()));
        when(userService.getUserByEmail(user.getEmail())).thenReturn(userDTO);

        ResultActions response = mockMvc.perform(
                post("/api/auth").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(json))
        );

        response
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(AuthenticateResponseMessages.AUTHENTICATION_SUCCESS));
    }

    @Test
    public void whenAuthenticateUserWithBadCredentials() throws Exception {
        Map<String, String> json = new HashMap<>();
        json.put("username", "noexists@noemail.com");
        json.put("password", "no_secret_password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(any(Authentication.class));
        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList()));
        when(userService.getUserByEmail(user.getEmail())).thenReturn(userDTO);

        ResultActions response = mockMvc.perform(
                post("/api/auth").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(json))
        );

        response.andExpect(status().isForbidden());
    }
}
