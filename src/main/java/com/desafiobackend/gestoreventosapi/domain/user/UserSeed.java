package com.desafiobackend.gestoreventosapi.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserSeed {

    @Autowired
    private UserRepository repository;

    private User user;

    public User create() {
        getData();
        Object result = repository.findByRoles(user.getRoles()).orElse(null);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        if(result == null) repository.save(user);
        return user;
    }

    public User getData() {
        user = new User();
        user.setName("SISTEMA");
        user.setEmail("sistema@sys.com");
        user.setRoles(Arrays.asList("SYSTEM","ADMIN"));
        user.setPassword("12345678");
        return user;
    }
}
