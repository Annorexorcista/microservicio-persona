package com.bootcamp.persona.infrastructure.adapters.driving.webflux.router;

import com.bootcamp.persona.domain.api.PersonServicePort;
import com.bootcamp.persona.infrastructure.adapters.driving.webflux.dto.ErrorResponse;
import com.bootcamp.persona.infrastructure.adapters.driving.webflux.dto.PersonEnrollmentRequest;
import com.bootcamp.persona.infrastructure.adapters.driving.webflux.dto.PersonEnrollmentResponse;
import com.bootcamp.persona.infrastructure.adapters.driving.webflux.handler.PersonHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class PersonRouter {

    private static final String ENROLLMENTS_PATH = "/api/v1/persons/enrollments";

    @Bean
    @RouterOperation(
            path = ENROLLMENTS_PATH,
            method = RequestMethod.POST,
            beanClass = PersonServicePort.class,
            beanMethod = "enroll",
            operation = @Operation(
                    operationId = "enrollPerson",
                    summary = "Inscribe una persona en un bootcamp",
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PersonEnrollmentRequest.class))),
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    description = "Inscripción creada",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = PersonEnrollmentResponse.class))),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Datos inválidos",
                                    content = @Content(
                                            schema = @Schema(implementation = ErrorResponse.class))),
                            @ApiResponse(
                                    responseCode = "409",
                                    description = "Conflicto de inscripción",
                                    content = @Content(
                                            schema = @Schema(implementation = ErrorResponse.class)))
                    }))
    public RouterFunction<ServerResponse> personRoutes(PersonHandler handler) {
        return RouterFunctions.route()
                .POST(ENROLLMENTS_PATH, accept(MediaType.APPLICATION_JSON), handler::enroll)
                .build();
    }
}
