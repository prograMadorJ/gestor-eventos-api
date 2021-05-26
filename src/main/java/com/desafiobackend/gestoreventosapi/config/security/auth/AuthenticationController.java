package com.desafiobackend.gestoreventosapi.config.security.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.desafiobackend.gestoreventosapi.config.security.service.UserDetailsServiceImpl;
import com.desafiobackend.gestoreventosapi.domain.user.UserService;
import com.desafiobackend.gestoreventosapi.utils.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.desafiobackend.gestoreventosapi.config.security.SecurityConstants.EXPIRATION_TIME;
import static com.desafiobackend.gestoreventosapi.config.security.SecurityConstants.SECRET;


@Api(value = "Autenticação", consumes = "application/json", produces = "application/json")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Autenticar usuário", notes = "Retorna a autenciação do usuário", response = RestResponse.class,
            consumes = "application/json", produces = "application/json")
    @PostMapping
    public ResponseEntity createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("invalid username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwtToken = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));

        return new RestResponse("authentication success", new AuthenticationResponse(jwtToken, userService.getUserByEmail(authenticationRequest.getUsername()))).status(HttpStatus.OK);
    }

}
