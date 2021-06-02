package com.desafiobackend.gestoreventosapi.domain.event;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends MongoRepository<Event, String> {

    @Query("{'user.$id': ObjectId(?0) }")
    Optional<List<Event>> findEventsByUserId(String id);
}
