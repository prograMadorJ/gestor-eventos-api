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

    public static String resultMessage = "";
    public static HttpStatus httpStatus = HttpStatus.NOT_IMPLEMENTED;
    public static Object content = null;


    public CaseResponse resultMessage(String message) {
        CaseResponse.resultMessage = message;
        return new CaseResponse(message);
    }

    public CaseResponse resultContent(Object content) {
        CaseResponse.content = content;
        return new CaseResponse(content);
    }

    public ResponseEntity buildResponse() {
        ResponseEntity result = new RestResponse(CaseResponse.resultMessage, CaseResponse.content).status(CaseResponse.httpStatus);
        CaseResponse.resultMessage = "";
        CaseResponse.httpStatus = HttpStatus.NOT_IMPLEMENTED;
        CaseResponse.content = null;
        return result;
    }

    public static class CaseResponse extends BaseResponseMessages {

        public CaseResponse(String message) {
            CaseResponse.resultMessage = message;
        }

        public CaseResponse(Object content) {
            CaseResponse.content = content;
        }

        public CaseResponse addCaseResponse(String message, HttpStatus httpStatus) {
            if (CaseResponse.resultMessage.equals(message)) {
                CaseResponse.resultMessage = message;
                CaseResponse.httpStatus = httpStatus;
            }
            return this;
        }

        public CaseResponse addCaseResponseContent(String message, HttpStatus httpStatus) {
            if (CaseResponse.resultMessage.equals(message) && !Objects.equals(CaseResponse.content, null)) {
                CaseResponse.resultMessage = message;
                CaseResponse.httpStatus = httpStatus;
            }
            return this;
        }

        public CaseResponse addCaseResponseContentNull(String message, HttpStatus httpStatus) {
            if (CaseResponse.resultMessage.equals(message) && Objects.equals(CaseResponse.content, null)) {
                CaseResponse.resultMessage = message;
                CaseResponse.httpStatus = httpStatus;
            }
            return this;
        }
    }
}
