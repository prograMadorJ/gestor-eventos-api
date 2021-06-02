package com.desafiobackend.gestoreventosapi.domain.user;

import com.desafiobackend.gestoreventosapi.base.BaseRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserRules extends BaseRules {

    @Autowired
    public IsNotValid isNotValid;


    @Service
    public static class IsNotValid extends BaseRules {

        public boolean whenUserIdIsNull(User user) {
            return rules.add(user.getId() == null);
        }

        public boolean whenUserIsNull(User user) {
            return rules.add(user == null);
        }

        public boolean whenUserExistsByEmail(Boolean resultFindUserByEmail) {
           return rules.add(Objects.equals(resultFindUserByEmail, true));
        }

        public boolean whenUserIsNotAuth(UserDTO userDTO) {
            return rules.add(userDTO == null);
        }

        public boolean whenUserAuthIsNotAdmin(boolean userAuthAdmin) {
            return rules.add(Objects.equals(userAuthAdmin, false));
        }

        public boolean whenUserAuthIsAdmin(boolean userAuthAdmin) {
            return rules.add(Objects.equals(userAuthAdmin, true));
        }

        public boolean whenUserAuthByEmailNotSameAsUser(String userAuthEmail, String userEmail) {
            return rules.add(!userAuthEmail.equals(userEmail));
        }

        public boolean whenUserAuthByEmailSameAsUser(String userAuthEmail, String userEmail) {
            return rules.add(userAuthEmail.equals(userEmail));
        }

        public boolean whenUserAuthWithRoleIsNotSystem(String userAuthRole) {
            return rules.add(!userAuthRole.contains("SYSTEM"));
        }

        public boolean whenUserAuthWithRoleIsSystemTryDeleteYourSelf(String userAuthRole, User resultFindUser) {
            String deleteUserRole = resultFindUser != null ? resultFindUser.getRoles().toString() : null;
            return rules.add(userAuthRole.contains("SYSTEM") && userAuthRole.equals(deleteUserRole));
        }

        public boolean whenUserHasOneOrManyEvent(List resultFindEventsByUserId) {
            boolean userHasEvents = resultFindEventsByUserId != null && resultFindEventsByUserId.size() > 0;
            return rules.add(userHasEvents);
        }

    }
}
