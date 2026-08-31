package com.bootcamp.persona.domain.usecase;

import com.bootcamp.persona.domain.api.PersonServicePort;
import com.bootcamp.persona.domain.exception.DomainErrorCode;
import com.bootcamp.persona.domain.exception.EnrollmentConflictException;
import com.bootcamp.persona.domain.exception.InvalidPersonDataException;
import com.bootcamp.persona.domain.model.BootcampInfo;
import com.bootcamp.persona.domain.model.Enrollment;
import com.bootcamp.persona.domain.model.EnrollmentSummary;
import com.bootcamp.persona.domain.model.Person;
import com.bootcamp.persona.domain.spi.BootcampGatewayPort;
import com.bootcamp.persona.domain.spi.PersonPersistencePort;
import com.bootcamp.persona.domain.spi.ReportGatewayPort;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class PersonUseCase implements PersonServicePort {

    private static final int MAX_BOOTCAMPS = 5;
    private static final int MAX_NAME_LENGTH = 100;

    private final PersonPersistencePort persistence;
    private final BootcampGatewayPort bootcampGateway;
    private final ReportGatewayPort reportGateway;

    public PersonUseCase(PersonPersistencePort persistence,
                         BootcampGatewayPort bootcampGateway,
                         ReportGatewayPort reportGateway) {
        this.persistence = persistence;
        this.bootcampGateway = bootcampGateway;
        this.reportGateway = reportGateway;
    }

    @Override
    public Mono<Enrollment> enroll(Person person, Long bootcampId) {
        return validate(person, bootcampId)
                .flatMap(input -> bootcampGateway.findById(input.bootcampId())
                        .switchIfEmpty(Mono.error(new InvalidPersonDataException(
                                DomainErrorCode.BOOTCAMP_NOT_FOUND)))
                        .flatMap(bootcamp -> persistence
                                .findEnrollmentsByEmail(input.person().getEmail())
                                .collectList()
                                .flatMap(existing -> validateAvailability(existing, bootcamp))
                                .flatMap(ignored -> saveEnrollment(input.person(), bootcamp))
                                .flatMap(saved -> reportGateway
                                        .publishEnrollment(input.person(), saved)
                                        .onErrorResume(error -> Mono.empty())
                                        .thenReturn(saved))));
    }

    private Mono<Enrollment> saveEnrollment(Person person, BootcampInfo bootcamp) {
        Enrollment enrollment = new Enrollment(
                null,
                null,
                bootcamp.getId(),
                bootcamp.getLaunchDate(),
                bootcamp.getDurationInDays(),
                LocalDate.now());
        return persistence.save(person, enrollment);
    }

    private Mono<EnrollmentInput> validate(Person person, Long bootcampId) {
        return Mono.defer(() -> {
            if (person == null) {
                return Mono.error(new InvalidPersonDataException(DomainErrorCode.PERSON_REQUIRED));
            }

            String name = trim(person.getName());
            String email = normalizeEmail(person.getEmail());

            if (name == null || name.isEmpty()) {
                return Mono.error(new InvalidPersonDataException(DomainErrorCode.NAME_REQUIRED));
            }
            if (name.length() > MAX_NAME_LENGTH) {
                return Mono.error(new InvalidPersonDataException(DomainErrorCode.NAME_TOO_LONG));
            }
            if (email == null || email.isEmpty()) {
                return Mono.error(new InvalidPersonDataException(DomainErrorCode.EMAIL_REQUIRED));
            }
            if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                return Mono.error(new InvalidPersonDataException(DomainErrorCode.EMAIL_INVALID));
            }
            if (bootcampId == null || bootcampId <= 0) {
                return Mono.error(new InvalidPersonDataException(
                        DomainErrorCode.BOOTCAMP_ID_INVALID));
            }

            return Mono.just(new EnrollmentInput(
                    new Person(person.getId(), name, email), bootcampId));
        });
    }

    private Mono<Void> validateAvailability(List<EnrollmentSummary> existing,
                                             BootcampInfo bootcamp) {
        if (existing.size() >= MAX_BOOTCAMPS) {
            return Mono.error(new EnrollmentConflictException(
                    DomainErrorCode.MAX_BOOTCAMPS_REACHED));
        }
        if (existing.stream().anyMatch(enrollment -> enrollment.getBootcampId()
                .equals(bootcamp.getId()))) {
            return Mono.error(new EnrollmentConflictException(
                    DomainErrorCode.DUPLICATE_ENROLLMENT));
        }

        LocalDate start = bootcamp.getLaunchDate();
        LocalDate end = start.plusDays(bootcamp.getDurationInDays() - 1L);
        boolean overlaps = existing.stream().anyMatch(enrollment ->
                start.compareTo(enrollment.getEndDate()) <= 0
                        && enrollment.getLaunchDate().compareTo(end) <= 0);

        return overlaps
                ? Mono.error(new EnrollmentConflictException(
                DomainErrorCode.OVERLAPPING_ENROLLMENT))
                : Mono.empty();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeEmail(String value) {
        String trimmed = trim(value);
        return trimmed == null ? null : trimmed.toLowerCase(Locale.ROOT);
    }

    private record EnrollmentInput(Person person, Long bootcampId) {
    }
}
