package com.desafiobackend.gestoreventosapi.domain.event;

import com.desafiobackend.gestoreventosapi.base.BaseResponseMessages;

public class EventResponseMessages extends BaseResponseMessages {

    public EventResponseMessages(String resultMessage) {
        super(resultMessage);
    }

    public EventResponseMessages(String resultMessage, Object content) {
        super(resultMessage, content);
    }

    public static final String USER_NOT_EXISTS = "user not exists";
}
