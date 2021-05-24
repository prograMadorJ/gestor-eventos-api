package com.desafiobackend.gestoreventosapi.event;

import com.desafiobackend.gestoreventosapi.base.DTO;
import com.desafiobackend.gestoreventosapi.user.UserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventDTO extends DTO {

    @Id
    private String id;

    private String name;

    private Date date;

    @DBRef
    private UserDTO user;

}
