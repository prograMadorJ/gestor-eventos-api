package com.desafiobackend.gestoreventosapi.domain.user;

import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import com.desafiobackend.gestoreventosapi.domain.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends RestServiceBaseImpl<User, UserDTO> {

    @Autowired
    private UserRules userRules;

    private final UserRepository userRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
        this.userRepository = repository;
    }

    @Override
    public String create(User user) {
        User userCreate = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (
                userRules.isNotValid
                        .whenUserExistsByEmail(userCreate)
                        .fullValidate()
        ) return UserResponseMessages.CONFLICT;
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole("USER");
        return super.create(user);
    }

    @Override
    public String update(String id, User user) {
        UserDTO userAuth = getUserAuthenticated();
        if(
                userRules.isNotValid
                .whenUserAuthIsAdmin(userAuth.isAdmin())
                .whenUserAuthByEmailSameAsUser(userAuth.getEmail(), user.getEmail())
                .fullValidate()
        ) {
            return UserResponseMessages.CONFLICT;
        } else if (
                userRules.isNotValid
                        .whenUserAuthIsNotAdmin(userAuth.isAdmin())
                        .whenUserAuthByEmailNotSameAsUser(userAuth.getEmail(), user.getEmail())
                        .whenUserExistsByEmail(userRepository.findByEmail(user.getEmail()).orElse(null))
                        .fullValidate()
        ) return UserResponseMessages.CONFLICT;
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return super.update(id, user);
    }

    @Override
    public String delete(String id) {
        UserDTO userAuth = getUserAuthenticated();
        User userDelete = userRepository.findById(id).orElse(null);
        if(
                userRules.isNotValid
                        .whenUserHasOneOrManyEvent(eventService.getEventsByUserId(id))
                .fullValidate()
        ) return UserResponseMessages.CANNOT_DELETE_HAS_EVENTS;

        else if(
                userRules.isNotValid
                        .whenUserAuthWithRoleIsNotSystem(userAuth.getRole())
                        .whenUserAuthWithRoleIsSystemTryDeleteYourSelf(userAuth.getRole(), userDelete)
                        .validateAtLeastOne()
        ) {
            return UserResponseMessages.CANNOT_DELETE;
        }
        return super.delete(id);
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
