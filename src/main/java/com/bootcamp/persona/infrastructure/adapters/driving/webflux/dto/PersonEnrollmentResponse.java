package com.bootcamp.persona.infrastructure.adapters.driving.webflux.dto;

import com.bootcamp.persona.domain.model.Enrollment;

import java.time.LocalDate;

public record PersonEnrollmentResponse(Long enrollmentId, Long personId, Long bootcampId, LocalDate launchDate, int durationDays, LocalDate enrolledAt) {
    public static PersonEnrollmentResponse from(Enrollment e) { return new PersonEnrollmentResponse(e.getId(), e.getPersonId(), e.getBootcampId(), e.getLaunchDate(), e.getDurationDays(), e.getEnrolledAt()); }
}
