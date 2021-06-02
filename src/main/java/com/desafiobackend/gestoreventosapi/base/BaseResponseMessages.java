package com.desafiobackend.gestoreventosapi.base;

import com.desafiobackend.gestoreventosapi.utils.RestResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@Data
public abstract class BaseResponseMessages {

    public static final String CREATED = "create success";
    public static final String UPDATED = "updated success";
    public static final String DELETED = "delete success";
    public static final String CONFLICT = "conflict";
    public static final String FOUND = "found";
    public static final String NOT_FOUND = "not found";
    public static final String CREATE_ERROR = "create error";
    public static final String UPDATED_ERROR = "update error";
    public static final String DELETE_ERROR = "delete error";
    public static final String CANNOT_DELETE = "conditions not allowed delete";
    public static final String GIVEN_NULL = "the value provided must not be null";
    public static final String AUTH_REQUIRED = "authentication required";
    public static final String ID_NULL = "the id value provided must not be null";

    public static String resultMessage = "";
    public static HttpStatus httpStatus = HttpStatus.NOT_IMPLEMENTED;
    public static Object content = null;


    public BaseResponseMessages() {
    }

    public BaseResponseMessages(String resultMessage) {
        BaseResponseMessages.resultMessage = resultMessage;
    }

    public BaseResponseMessages(String resultMessage, Object content) {
        BaseResponseMessages.resultMessage = resultMessage;
        BaseResponseMessages.content = content;
    }


    public ResponseEntity buildResponse() {
        ResponseEntity result = new RestResponse(BaseResponseMessages.resultMessage, BaseResponseMessages.content).status(BaseResponseMessages.httpStatus);
        BaseResponseMessages.resultMessage = "";
        BaseResponseMessages.httpStatus = HttpStatus.NOT_IMPLEMENTED;
        BaseResponseMessages.content = null;
        return result;
    }

    public void addCaseResponse(String message, HttpStatus httpStatus) {
        if (BaseResponseMessages.resultMessage.equals(message)) {
            BaseResponseMessages.resultMessage = message;
            BaseResponseMessages.httpStatus = httpStatus;
        }
    }


    public void addCaseResponseContent(String message, HttpStatus httpStatus) {
        if (!Objects.equals(BaseResponseMessages.content, null)) {
            BaseResponseMessages.resultMessage = message;
            BaseResponseMessages.httpStatus = httpStatus;
        }
    }

    public void addCaseResponseContentNull(String message, HttpStatus httpStatus) {
        if (Objects.equals(BaseResponseMessages.content, null)) {
            BaseResponseMessages.resultMessage = message;
        BaseResponseMessages.httpStatus = httpStatus;
        }
    }
}
