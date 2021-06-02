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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class CreateCaseRestServiceBaseImpl {

    @Mock
    private MongoRepository repository;


    @InjectMocks
    private RestServiceBaseImpl restServiceBase;


    @Test
    public void whenCreateReturnCreatedMessage() {

        Object object = new Object();

        when(repository.save(object)).thenReturn(object);

        String result = restServiceBase.create(object);

        assertEquals(BaseResponseMessages.CREATED, result);
    }

    @Test
    public void whenCreateWithNullObjectReturnGivenNullMessage() {

        Object object = null;

        when(repository.save(object)).thenReturn(object);

        String result = restServiceBase.create(object);

        assertEquals(BaseResponseMessages.GIVEN_NULL, result);
    }

    @Test
    public void whenCreateWithNullObjectReturnCreateErrorMessage() {

        Object object = new Object();

        when(repository.save(object)).thenReturn(null);

        String result = restServiceBase.create(object);

        assertEquals(BaseResponseMessages.CREATE_ERROR, result);
    }
}
