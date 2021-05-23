package com.desafiobackend.gestoreventosapi.utils;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class RestResponse {

    private String message;

    private Object content;

    public RestResponse(String message) {
        this.message = message;
    }

    public RestResponse(String message, Object data) {
        this.message = message;
        this.content = data;
    }

    public ResponseEntity status(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(this);
    }
}
