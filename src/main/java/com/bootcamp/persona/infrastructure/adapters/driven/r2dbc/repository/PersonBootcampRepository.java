package com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.repository;

import com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.entity.PersonBootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PersonBootcampRepository extends ReactiveCrudRepository<PersonBootcampEntity, Long> {
    Flux<PersonBootcampEntity> findByPersonId(Long personId);
}
