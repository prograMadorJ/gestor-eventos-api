package com.desafiobackend.gestoreventosapi.base.cases;

import com.desafiobackend.gestoreventosapi.base.BaseResponseMessages;
import com.desafiobackend.gestoreventosapi.base.RestServiceBaseImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class DeleteCaseRestServiceBaseImpl {

    @Mock
    private MongoRepository repository;


    @InjectMocks
    private RestServiceBaseImpl restServiceBase;


    @Test
    public void whenDeleteReturnDeletedMessage() {

        Object object = new Object();

        when(repository.findById("").orElse(null)).thenReturn(Optional.of(object));

        String result = restServiceBase.delete("");

        assertEquals(BaseResponseMessages.DELETED, result);
    }

    @Test
    public void whenDeleteWithIdNullReturnIdNullMessage() {

        String result = restServiceBase.delete(null);

        assertEquals(BaseResponseMessages.ID_NULL, result);
    }

    @Test
    public void whenDeleteWithIsNotExistsObjectReturnNotFoundMessage() {

        when(repository.findById("").orElse(null)).thenReturn(Optional.empty());

        String result = restServiceBase.delete("");

        assertEquals(BaseResponseMessages.NOT_FOUND, result);
    }
}
