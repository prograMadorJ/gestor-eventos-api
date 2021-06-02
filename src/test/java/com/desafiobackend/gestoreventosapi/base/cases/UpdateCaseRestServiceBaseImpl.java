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
public class UpdateCaseRestServiceBaseImpl {

    @Mock
    private MongoRepository repository;


    @InjectMocks
    private RestServiceBaseImpl restServiceBase;


    @Test
    public void whenUpdateReturnUpdatedMessage() {

        Object object = new Object();

        when(repository.findById("").orElse(null)).thenReturn(Optional.of(object));

        String result = restServiceBase.update("", object);

        assertEquals(BaseResponseMessages.UPDATED, result);
    }

    @Test
    public void whenUpdateWithNullObjectReturnGivenNullMessage() {

        Object object = null;

        String result = restServiceBase.update("", object);

        assertEquals(BaseResponseMessages.GIVEN_NULL, result);
    }

    @Test
    public void whenUpdateWithIdNullReturnIdNullMessage() {

        Object object = new Object();

        String result = restServiceBase.update(null, object);

        assertEquals(BaseResponseMessages.ID_NULL, result);
    }

    @Test
    public void whenUpdateWithIsNotExistsObjectReturnNotFoundMessage() {

        Object object = new Object();

        when(repository.findById("").orElse(null)).thenReturn(Optional.empty());

        String result = restServiceBase.update("", object);

        assertEquals(BaseResponseMessages.NOT_FOUND, result);
    }
}
