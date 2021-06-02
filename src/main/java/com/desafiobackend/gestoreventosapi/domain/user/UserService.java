package com.desafiobackend.gestoreventosapi.domain.user;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import com.desafiobackend.gestoreventosapi.base.RestServiceBaseInterface;
import com.desafiobackend.gestoreventosapi.domain.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements RestServiceBaseInterface<User, UserDTO>{


    @Autowired
    private UserRules.IsNotValid notValid;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventService eventService;

    public RestServiceBaseImpl<User, UserDTO> restServiceBase;

    @Autowired
    public void init() {
        restServiceBase = new RestServiceBaseImpl<>(userRepository);
    }

    @Override
    public String create(User user) {
        if (
                notValid.whenUserExistsByEmail(userRepository.findByEmail(user.getEmail()).isPresent()) &&
                        notValid.fullValidate()
        ) return UserResponseMessages.CONFLICT;
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles(Collections.singletonList("USER"));
        return restServiceBase.create(user);
    }

    @Override
    public String update(String id, User user) {
        UserDTO userAuth = getUserAuthenticated();

        if(
                notValid.whenUserAuthIsAdmin(userAuth.isAdmin()) &&
                        notValid.whenUserAuthByEmailSameAsUser(userAuth.getEmail(), user.getEmail()) &&
                        notValid.fullValidate()
        ) {
            return UserResponseMessages.CONFLICT;

        } else if (
                notValid.whenUserAuthIsNotAdmin(userAuth.isAdmin()) &&
                        notValid.whenUserAuthByEmailNotSameAsUser(userAuth.getEmail(), user.getEmail()) &&
                        notValid.whenUserExistsByEmail(userRepository.findByEmail(user.getEmail()).isPresent()) &&
                        notValid.fullValidate()
        )
            return UserResponseMessages.CONFLICT;

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return restServiceBase.update(id, user);
    }

    @Override
    public String delete(String id) {
        UserDTO userAuth = getUserAuthenticated();
        User userDelete = userRepository.findByIdProjectRole(id).orElse(null);
        if(
                notValid.whenUserHasOneOrManyEvent(eventService.getEventsByUserId(id)) &&
                        notValid.fullValidate()
        ) return UserResponseMessages.CANNOT_DELETE_HAS_EVENTS;

        else if(
                notValid.whenUserAuthWithRoleIsNotSystem(userAuth.getRoles().toString()) &&
                        notValid.whenUserAuthWithRoleIsSystemTryDeleteYourSelf(userAuth.getRoles().toString(), userDelete) &&
                        notValid.validateAtLeastOne()
        ) {
            return UserResponseMessages.CANNOT_DELETE;
        }
        return restServiceBase.delete(id);
    }

    @Override
    public User getOne(String id) {
        return restServiceBase.getOne(id);
    }

    @Override
    public UserDTO getOne(String id, Class<? extends UserDTO> clazz) {
        return restServiceBase.getOne(id, clazz);
    }

    @Override
    public List<User> getAll() {
        return restServiceBase.getAll();
    }

    @Override
    public List<UserDTO> getAll(Class<? extends UserDTO> clazz) {
        return restServiceBase.getAll(clazz);
    }

    @Override
    public UserDTO toDTO(User user, Class<? extends UserDTO> clazz) {
        return restServiceBase.toDTO(user, clazz);
    }

    @Override
    public User toEntity(UserDTO userDTO, Class<? extends User> clazz) {
        return restServiceBase.toEntity(userDTO, clazz);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null ? toDTO(user, UserDTO.class) : null;
    }

    public UserDTO getUserAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) throw new BadCredentialsException("missing authentication");
        Object principal = auth.getPrincipal();
        String username = (principal instanceof UserDetails)
                ?  ((UserDetails) principal).getUsername()
                : principal.toString();
        return getUserByEmail(username);
    }
}
