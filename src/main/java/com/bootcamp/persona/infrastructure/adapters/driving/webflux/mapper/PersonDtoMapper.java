package com.bootcamp.persona.infrastructure.adapters.driving.webflux.mapper;

import com.bootcamp.persona.domain.model.Person;
import com.bootcamp.persona.infrastructure.adapters.driving.webflux.dto.PersonEnrollmentRequest;

public class PersonDtoMapper {
    public Person toDomain(PersonEnrollmentRequest request) { return new Person(null, request.name(), request.email()); }
}
