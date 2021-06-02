package com.desafiobackend.gestoreventosapi.domain.event;

import com.desafiobackend.gestoreventosapi.domain.user.User;
import com.desafiobackend.gestoreventosapi.domain.user.UserResponseMessages;
import com.desafiobackend.gestoreventosapi.domain.user.UserSeed;
import com.desafiobackend.gestoreventosapi.utils.RestResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@EnabledIf(
        expression = "${tests.mvc.enabled}",
        reason = "MVC test only at local development",
        loadContext = true)
@SpringBootTest
@AutoConfigureMockMvc
public class EventCaseAPI {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserSeed userSeed;

    @Autowired
    private ObjectMapper objectMapper;

    private static HttpHeaders authorizationHeader;

    @BeforeEach
    public void setUp() throws Exception {
        authorizationHeader = getAuthorizationHeader(userSeed.getData());
    }

    private HttpHeaders getAuthorizationHeader(User user) throws Exception {
        Map<String, String> json = new HashMap<>();
        json.put("username", user.getEmail());
        json.put("password", user.getPassword());

        ResultActions result = mockMvc.perform(
                post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(json))
        );



        JwtTokenDTO jwtTokenDTO = getParser(result, JwtTokenDTO.class);


        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization","Bearer " + jwtTokenDTO.getJwt());

        return headers;
    }

    private <T> T getParser(ResultActions result, Class<T> clazz) throws UnsupportedEncodingException, JsonProcessingException {
        RestResponse response = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), RestResponse.class);

        String jsonContent = objectMapper.writeValueAsString(response.getContent());

        return objectMapper.readValue(jsonContent, clazz);
    }

    @Test
    public void CRUDTest() throws Exception {

        /*
            CREATE USER
         */
        User user = new User(
                null,
                "Create User",
                "createuser@email.com",
                "secret_password",
                Collections.singletonList("USER")
        );

        ResultActions resultCreateUserActions = createUser(user);
        user.setId(getParser(resultCreateUserActions, String.class));


        /*
            CREATE
         */

        Event event = new Event(
                null,
                "New Event",
                new Date(),
                user

        );


        ResultActions resultCreateActions = createEvent(event);

        /*
            GET ALL
         */
        getEvents();


        /*
            GET ONE
         */

        event.setId(getParser(resultCreateActions, String.class));

        getEvent(event);

        /*
            UPDATE
         */

        event.setName("Updated Event");

        updateEvent(event);

        /*
            DELETE
         */
        deleteEvent(event);


        /*
            DELETE USER
         */
        deleteUser(user);


    }


    private ResultActions createEvent(Event event) throws Exception {

        return  mockMvc.perform(
                post("/api/events")
                        .headers(authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(EventResponseMessages.CREATED));

    }

    private void getEvents() throws Exception {
        mockMvc.perform(
                get("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(authorizationHeader)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(EventResponseMessages.FOUND));


    }


    private void getEvent(Event event) throws Exception {
        mockMvc.perform(
                get("/api/events/" + event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(authorizationHeader)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(EventResponseMessages.FOUND));

    }

    private void updateEvent(Event event) throws Exception {
        mockMvc.perform(
                put("/api/events/")
                        .headers(authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(EventResponseMessages.UPDATED));

    }


    private void deleteEvent(Event event) throws Exception {

        mockMvc.perform(
                delete("/api/events/" + event.getId())
                        .headers(authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(EventResponseMessages.DELETED));

    }


    private ResultActions createUser(User user) throws Exception {

        return  mockMvc.perform(
                post("/api/users")
                        .headers(authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(UserResponseMessages.CREATED));

    }

    private void deleteUser(User user) throws Exception {

        mockMvc.perform(
                delete("/api/users/" + user.getId())
                        .headers(authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(UserResponseMessages.DELETED));

    }

    static class JwtTokenDTO {

        private String jwt;

        public void setJwt(String jwt) {
            this.jwt = jwt;
        }

        public String getJwt() {
            return jwt;
        }
    }

}
