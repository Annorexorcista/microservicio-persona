package com.bootcamp.persona.domain.spi;

import com.bootcamp.persona.domain.model.Enrollment;
import com.bootcamp.persona.domain.model.EnrollmentSummary;
import com.bootcamp.persona.domain.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonPersistencePort {
    Flux<EnrollmentSummary> findEnrollmentsByEmail(String email);
    Mono<Enrollment> save(Person person, Enrollment enrollment);
}
