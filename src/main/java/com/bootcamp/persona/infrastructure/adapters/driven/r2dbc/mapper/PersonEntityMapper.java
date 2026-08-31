package com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.mapper;

import com.bootcamp.persona.domain.model.Enrollment;
import com.bootcamp.persona.domain.model.EnrollmentSummary;
import com.bootcamp.persona.domain.model.Person;
import com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.entity.PersonBootcampEntity;
import com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.entity.PersonEntity;

public class PersonEntityMapper {

    public PersonEntity toEntity(Person person) {
        return new PersonEntity(person.getId(), person.getName(), person.getEmail());
    }

    public PersonBootcampEntity toEntity(Enrollment enrollment) {
        return new PersonBootcampEntity(
                enrollment.getId(),
                enrollment.getPersonId(),
                enrollment.getBootcampId(),
                enrollment.getLaunchDate(),
                enrollment.getDurationDays(),
                enrollment.getEnrolledAt());
    }

    public Enrollment toDomain(PersonBootcampEntity entity) {
        return new Enrollment(
                entity.getId(),
                entity.getPersonId(),
                entity.getBootcampId(),
                entity.getLaunchDate(),
                entity.getDurationDays(),
                entity.getEnrolledAt());
    }

    public EnrollmentSummary toSummary(PersonBootcampEntity entity) {
        return new EnrollmentSummary(
                entity.getBootcampId(),
                entity.getLaunchDate(),
                entity.getDurationDays());
    }
}
