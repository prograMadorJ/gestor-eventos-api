package com.desafiobackend.gestoreventosapi.domain.event;

import com.desafiobackend.gestoreventosapi.base.BaseRules;
import com.desafiobackend.gestoreventosapi.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventRules extends BaseRules {

    @Autowired
    public IsNotValid isNotValid;

    @Service
    public static class IsNotValid extends EventRules {

        public IsNotValid whenUserNotExists(User resultFindUser) {
            rules.add(resultFindUser == null);
            return this;
        }

    }

}
