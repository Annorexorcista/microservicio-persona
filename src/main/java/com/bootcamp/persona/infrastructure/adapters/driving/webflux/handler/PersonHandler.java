package com.bootcamp.persona.infrastructure.adapters.driving.webflux.handler;

import com.bootcamp.persona.domain.api.PersonServicePort;
import com.bootcamp.persona.domain.exception.DomainErrorCode;
import com.bootcamp.persona.domain.exception.InvalidPersonDataException;
import com.bootcamp.persona.infrastructure.adapters.driving.webflux.dto.PersonEnrollmentRequest;
import com.bootcamp.persona.infrastructure.adapters.driving.webflux.dto.PersonEnrollmentResponse;
import com.bootcamp.persona.infrastructure.adapters.driving.webflux.mapper.PersonDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class PersonHandler {

    private final PersonServicePort service;
    private final PersonDtoMapper mapper;

    public PersonHandler(PersonServicePort service, PersonDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    public Mono<ServerResponse> enroll(ServerRequest request) {
        return request.bodyToMono(PersonEnrollmentRequest.class)
                .switchIfEmpty(Mono.error(new InvalidPersonDataException(
                        DomainErrorCode.PERSON_REQUIRED)))
                .flatMap(body -> service.enroll(mapper.toDomain(body), body.bootcampId()))
                .map(PersonEnrollmentResponse::from)
                .flatMap(response -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }
}
