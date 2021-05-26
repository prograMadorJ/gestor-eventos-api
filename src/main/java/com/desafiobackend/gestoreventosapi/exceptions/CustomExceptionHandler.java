package com.desafiobackend.gestoreventosapi.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity handleAccessDeniedException(Exception ex) {
        return new ResponseEntity<Object>(ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

}
