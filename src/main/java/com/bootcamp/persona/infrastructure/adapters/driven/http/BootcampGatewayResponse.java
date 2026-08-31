package com.bootcamp.persona.infrastructure.adapters.driven.http;

import java.time.LocalDate;
import java.util.List;

public record BootcampGatewayResponse(Long id, String name, String description, LocalDate launchDate, int durationInDays, List<Long> capabilityIds) { }
