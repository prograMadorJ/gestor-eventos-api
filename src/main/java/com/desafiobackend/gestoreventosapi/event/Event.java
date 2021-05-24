package com.desafiobackend.gestoreventosapi.event;

import com.desafiobackend.gestoreventosapi.user.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document("events")
public class Event {

    @Id
    private String id;

    private String name;

    private Date date;

    @DBRef
    private User user;

}
