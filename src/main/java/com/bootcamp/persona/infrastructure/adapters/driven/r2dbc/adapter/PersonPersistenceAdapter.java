package com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.adapter;

import com.bootcamp.persona.domain.exception.DomainErrorCode;
import com.bootcamp.persona.domain.exception.EnrollmentConflictException;
import com.bootcamp.persona.domain.model.Enrollment;
import com.bootcamp.persona.domain.model.EnrollmentSummary;
import com.bootcamp.persona.domain.model.Person;
import com.bootcamp.persona.domain.spi.PersonPersistencePort;
import com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.entity.PersonEntity;
import com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.mapper.PersonEntityMapper;
import com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.repository.PersonBootcampRepository;
import com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.repository.PersonRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonPersistenceAdapter implements PersonPersistencePort {

    private final PersonRepository persons;
    private final PersonBootcampRepository enrollments;
    private final PersonEntityMapper mapper;
    private final TransactionalOperator transaction;

    public PersonPersistenceAdapter(PersonRepository persons,
                                    PersonBootcampRepository enrollments,
                                    PersonEntityMapper mapper,
                                    TransactionalOperator transaction) {
        this.persons = persons;
        this.enrollments = enrollments;
        this.mapper = mapper;
        this.transaction = transaction;
    }

    @Override
    public Flux<EnrollmentSummary> findEnrollmentsByEmail(String email) {
        return persons.findByEmailIgnoreCase(email)
                .flatMapMany(person -> enrollments.findByPersonId(person.getId()))
                .map(mapper::toSummary);
    }

    @Override
    public Mono<Enrollment> save(Person person, Enrollment enrollment) {
        Mono<PersonEntity> personEntity = persons.findByEmailIgnoreCase(person.getEmail())
                .switchIfEmpty(persons.save(mapper.toEntity(person)));

        Mono<Enrollment> pipeline = personEntity
                .flatMap(savedPerson -> {
                    Enrollment linkedEnrollment = new Enrollment(
                            enrollment.getId(),
                            savedPerson.getId(),
                            enrollment.getBootcampId(),
                            enrollment.getLaunchDate(),
                            enrollment.getDurationDays(),
                            enrollment.getEnrolledAt());
                    return enrollments.save(mapper.toEntity(linkedEnrollment));
                })
                .map(mapper::toDomain);

        return pipeline
                .as(transaction::transactional)
                .onErrorMap(DataIntegrityViolationException.class,
                        error -> new EnrollmentConflictException(
                                DomainErrorCode.PERSISTENCE_CONFLICT));
    }
}
