package com.bootcamp.persona.domain.api;

import com.bootcamp.persona.domain.model.Enrollment;
import com.bootcamp.persona.domain.model.Person;
import reactor.core.publisher.Mono;

public interface PersonServicePort {
    Mono<Enrollment> enroll(Person person, Long bootcampId);
}
