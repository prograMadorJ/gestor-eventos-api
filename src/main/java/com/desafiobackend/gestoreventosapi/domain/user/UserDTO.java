package com.desafiobackend.gestoreventosapi.domain.user;

import com.desafiobackend.gestoreventosapi.base.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends DTO {

    @Id
    private String id;

    private String name;

    private String email;

    private List<String> roles = new ArrayList<>();

    public boolean isAdmin() {
        return roles.contains("ADMIN");
    }
}
