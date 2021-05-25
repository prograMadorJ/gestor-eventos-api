package com.desafiobackend.gestoreventosapi.domain.user;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends RestServiceBaseImpl<User, UserDTO> {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
        this.userRepository = repository;
    }

    @Override
    public String create(User user) {
        User result = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (result != null) return "conflict";
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole("USER");
        return super.create(user);
    }

    @Override
    public String update(String id, User user) {
        UserDTO userAuth = getUserAuthenticated();
        if (!userAuth.isAdmin() && !userAuth.getEmail().equals(user.getEmail())) return "conflict";
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return super.update(id, user);
    }

    @Override
    public String delete(String id) {
        UserDTO userAuth = getUserAuthenticated();
        User userDTO = getOne(id);
        if(userAuth.getRole().equals(userDTO.getRole())) {
           return "no deleted";
        }
        return super.delete(id);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null ? toDTO(user, UserDTO.class) : null;
    }

    public UserDTO getUserAuthenticated() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails)
                ?  ((UserDetails) principal).getUsername()
                : principal.toString();
        return getUserByEmail(username);
    }
}
