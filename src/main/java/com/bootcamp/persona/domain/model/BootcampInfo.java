package com.bootcamp.persona.domain.model;

import java.time.LocalDate;
import java.util.List;

public final class BootcampInfo {
    private final Long id;
    private final String name;
    private final String description;
    private final LocalDate launchDate;
    private final int durationInDays;
    private final List<Long> capabilityIds;

    public BootcampInfo(Long id, String name, String description, LocalDate launchDate,
                        int durationInDays, List<Long> capabilityIds) {
        this.id = id; this.name = name; this.description = description;
        this.launchDate = launchDate; this.durationInDays = durationInDays;
        this.capabilityIds = capabilityIds == null ? List.of() : List.copyOf(capabilityIds);
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDate getLaunchDate() { return launchDate; }
    public int getDurationInDays() { return durationInDays; }
    public List<Long> getCapabilityIds() { return capabilityIds; }
}
