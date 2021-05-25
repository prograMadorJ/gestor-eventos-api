package com.desafiobackend.gestoreventosapi.config.security.auth;

import com.desafiobackend.gestoreventosapi.domain.user.UserDTO;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private final String jwt;

    private final UserDTO user;

    public AuthenticationResponse(String jwt, UserDTO user) {
        this.jwt = jwt;
        this.user = user;
    }
}
