package com.desafiobackend.gestoreventosapi.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "{'id': ?0}", fields = "{'role': 1}")
    Optional<User> findByIdProjectRole(String id);

    Optional<User> findByEmail(String email);

    Optional<User> findByRoles(List<String> roles);

}
