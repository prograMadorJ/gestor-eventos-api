package com.desafiobackend.gestoreventosapi.domain.user;

import com.desafiobackend.gestoreventosapi.base.BaseResponseMessages;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResponseMessages extends BaseResponseMessages {


    public UserResponseMessages(String resultMessage) {
        super(resultMessage);
    }

    public UserResponseMessages(String resultMessage, Object content) {
        super(resultMessage, content);
    }

    public static final String CANNOT_DELETE_HAS_EVENTS = "cannot delete because has events";

}
