package com.desafiobackend.gestoreventosapi.domain.user;

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
public class UserCaseAPI {

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
            CREATE
         */

        User user = new User(
                null,
                "Create User",
                "createuser@email.com",
                "secret_password",
                null
        );


        ResultActions resultCreateActions = createUser(user);

        /*
            GET ALL
         */
        getUsers();


        /*
            GET ONE
         */

        user.setId(getParser(resultCreateActions, String.class));

        getUser(user);

        /*
            UPDATE
         */

        user.setName("Updated User Name");
        user.setPassword("12345678");

        updateUser(user);

        /*
            DELETE
         */
        deleteUser(user);


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

    private void getUsers() throws Exception {
        mockMvc.perform(
                get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(authorizationHeader)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(UserResponseMessages.FOUND));


    }


    private void getUser(User user) throws Exception {
        mockMvc.perform(
                get("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(authorizationHeader)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(UserResponseMessages.FOUND));

    }

    private void updateUser(User user) throws Exception {
        mockMvc.perform(
                put("/api/users/")
                        .headers(authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(UserResponseMessages.UPDATED));

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
