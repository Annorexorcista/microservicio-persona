package com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.repository;

import com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.entity.PersonEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PersonRepository extends ReactiveCrudRepository<PersonEntity, Long> {
    Mono<PersonEntity> findByEmailIgnoreCase(String email);
}
