package com.bootcamp.persona.infrastructure.adapters.driven.http;

import com.bootcamp.persona.domain.model.Enrollment;
import com.bootcamp.persona.domain.model.Person;
import com.bootcamp.persona.domain.spi.ReportGatewayPort;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ReportGatewayAdapter implements ReportGatewayPort {
    private final WebClient client;

    public ReportGatewayAdapter(WebClient client) {
        this.client = client;
    }

    @Override
    public Mono<Void> publishEnrollment(Person person, Enrollment enrollment) {
        ReportEnrollmentEvent event = new ReportEnrollmentEvent(
                "PERSON_ENROLLED-" + enrollment.getPersonId() + "-" + enrollment.getBootcampId(),
                "PERSON_ENROLLED",
                enrollment.getBootcampId(),
                person.getName(),
                person.getEmail());
        return client.post()
                .uri("/api/v1/reports/events")
                .bodyValue(event)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private record ReportEnrollmentEvent(
            String eventId,
            String eventType,
            Long bootcampId,
            String personName,
            String personEmail) {
    }
}
