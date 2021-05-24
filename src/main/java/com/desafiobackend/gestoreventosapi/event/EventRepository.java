package com.desafiobackend.gestoreventosapi.event;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface EventRepository extends MongoRepository<Event, String> {
}
