package com.bootcamp.persona.domain.model;

import java.time.LocalDate;

public final class EnrollmentSummary {

    private final Long bootcampId;
    private final LocalDate launchDate;
    private final int durationDays;

    public EnrollmentSummary(Long bootcampId, LocalDate launchDate, int durationDays) {
        this.bootcampId = bootcampId;
        this.launchDate = launchDate;
        this.durationDays = durationDays;
    }

    public Long getBootcampId() {
        return bootcampId;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public LocalDate getEndDate() {
        return launchDate.plusDays(durationDays - 1L);
    }
}
