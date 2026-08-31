package com.bootcamp.persona.domain.spi;

import com.bootcamp.persona.domain.model.Enrollment;
import com.bootcamp.persona.domain.model.Person;
import reactor.core.publisher.Mono;

public interface ReportGatewayPort {
    Mono<Void> publishEnrollment(Person person, Enrollment enrollment);
}
