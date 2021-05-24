package com.desafiobackend.gestoreventosapi.user;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends RestServiceBaseImpl<User, UserDTO> {

    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
    }
}
