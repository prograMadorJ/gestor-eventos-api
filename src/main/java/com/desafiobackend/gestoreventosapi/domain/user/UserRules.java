package com.desafiobackend.gestoreventosapi.domain.user;

import com.desafiobackend.gestoreventosapi.base.BaseRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserRules extends BaseRules {

    @Autowired
    public IsNotValid isNotValid;

    @Service
    public static class IsNotValid extends UserRules {

        public IsNotValid whenUserExistsByEmail(User resultFindUserByEmail) {
            rules.add(resultFindUserByEmail != null);
            return this;
        }

        public IsNotValid whenUserAuthIsNotAdmin(boolean userAuthAdmin) {
            rules.add(Objects.equals(userAuthAdmin, false));
            return this;
        }

        public IsNotValid whenUserAuthIsAdmin(boolean userAuthAdmin) {
            rules.add(Objects.equals(userAuthAdmin, true));
            return this;
        }

        public IsNotValid whenUserAuthByEmailNotSameAsUser(String userAuthEmail, String userEmail) {
            rules.add(!userAuthEmail.equals(userEmail));
            return this;
        }

        public IsNotValid whenUserAuthByEmailSameAsUser(String userAuthEmail, String userEmail) {
            rules.add(userAuthEmail.equals(userEmail));
            return this;
        }

        public IsNotValid whenUserAuthWithRoleIsNotSystem(String userAuthRole) {
            rules.add(!userAuthRole.equals("SYSTEM"));
            return this;
        }

        public IsNotValid whenUserAuthWithRoleIsSystemTryDeleteYourSelf(String userAuthRole, User resultFindUser) {
            String deleteUserRole = resultFindUser != null ? resultFindUser.getRole() : null;
            rules.add(userAuthRole.equals("SYSTEM") && userAuthRole.equals(deleteUserRole));
            return this;
        }

        public IsNotValid whenUserHasOneOrManyEvent(List resultFindEventsByUserId) {
            boolean userHasEvents = resultFindEventsByUserId != null && resultFindEventsByUserId.size() > 0;
            rules.add(userHasEvents);
            return this;
        }

    }
}
