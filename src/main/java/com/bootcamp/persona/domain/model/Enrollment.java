package com.bootcamp.persona.domain.model;

import java.time.LocalDate;

public final class Enrollment {
    private final Long id;
    private final Long personId;
    private final Long bootcampId;
    private final LocalDate launchDate;
    private final int durationDays;
    private final LocalDate enrolledAt;

    public Enrollment(Long id, Long personId, Long bootcampId, LocalDate launchDate,
                      int durationDays, LocalDate enrolledAt) {
        this.id = id; this.personId = personId; this.bootcampId = bootcampId;
        this.launchDate = launchDate; this.durationDays = durationDays; this.enrolledAt = enrolledAt;
    }
    public Long getId() { return id; }
    public Long getPersonId() { return personId; }
    public Long getBootcampId() { return bootcampId; }
    public LocalDate getLaunchDate() { return launchDate; }
    public int getDurationDays() { return durationDays; }
    public LocalDate getEnrolledAt() { return enrolledAt; }
}
