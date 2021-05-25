package com.desafiobackend.gestoreventosapi.domain.user;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends RestServiceBaseImpl<User, UserDTO> {

    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
    }

    @Override
    public void create(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        super.create(user);
    }

    @Override
    public String update(String id, User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return super.update(id, user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = ((UserRepository) repository).findByEmail(email).orElse(null);
        return user != null ? toDTO(user, UserDTO.class) : null;
    }
}
