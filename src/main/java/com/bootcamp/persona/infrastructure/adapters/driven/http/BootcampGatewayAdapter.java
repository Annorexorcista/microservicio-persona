package com.bootcamp.persona.infrastructure.adapters.driven.http;

import com.bootcamp.persona.domain.exception.BootcampServiceException;
import com.bootcamp.persona.domain.exception.DomainErrorCode;
import com.bootcamp.persona.domain.exception.InvalidPersonDataException;
import com.bootcamp.persona.domain.model.BootcampInfo;
import com.bootcamp.persona.domain.spi.BootcampGatewayPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BootcampGatewayAdapter implements BootcampGatewayPort {

    private final WebClient client;

    public BootcampGatewayAdapter(WebClient client) {
        this.client = client;
    }

    @Override
    public Mono<BootcampInfo> findById(Long id) {
        return client.get()
                .uri("/api/v1/bootcamps/{id}", id)
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        response -> Mono.error(new InvalidPersonDataException(
                                DomainErrorCode.BOOTCAMP_NOT_FOUND)))
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new BootcampServiceException(
                                DomainErrorCode.BOOTCAMP_SERVICE_UNAVAILABLE,
                                new IllegalStateException(
                                        "No fue posible consultar Bootcamp_Service"))))
                .bodyToMono(BootcampGatewayResponse.class)
                .map(this::toDomain)
                .onErrorMap(error -> error instanceof InvalidPersonDataException
                        || error instanceof BootcampServiceException
                        ? error
                        : new BootcampServiceException(
                                DomainErrorCode.BOOTCAMP_SERVICE_UNAVAILABLE, error));
    }

    private BootcampInfo toDomain(BootcampGatewayResponse response) {
        return new BootcampInfo(
                response.id(),
                response.name(),
                response.description(),
                response.launchDate(),
                response.durationInDays(),
                response.capabilityIds());
    }
}
