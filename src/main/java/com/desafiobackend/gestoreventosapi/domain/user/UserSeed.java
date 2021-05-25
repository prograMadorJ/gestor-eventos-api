package com.desafiobackend.gestoreventosapi.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserSeed {

    @Autowired
    private UserRepository repository;

    public void create() {
        User user = new User();
        user.setName("SISTEMA");
        user.setEmail("sistema@sys.com");
        user.setAdmin(true);
        user.setRole("SYSTEM");
        user.setPassword(new BCryptPasswordEncoder().encode("12345678"));
        Object result = repository.findByEmail(user.getEmail()).orElse(null);
        if(result == null) repository.save(user);
    }
}
