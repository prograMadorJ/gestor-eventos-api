package com.desafiobackend.gestoreventosapi.config.security.service;

import com.desafiobackend.gestoreventosapi.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.desafiobackend.gestoreventosapi.domain.user.User user = userRepository.findByEmail(username).orElse(null);
        String password = user != null ? user.getPassword() : null;
        return new User(username, password, Collections.emptyList());
    }
}
