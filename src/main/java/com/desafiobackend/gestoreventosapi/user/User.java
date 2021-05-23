package com.desafiobackend.gestoreventosapi.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("users")
public class User {

    @Id
    private String id;

    private String name;

    private String email;

    private String password;
}
