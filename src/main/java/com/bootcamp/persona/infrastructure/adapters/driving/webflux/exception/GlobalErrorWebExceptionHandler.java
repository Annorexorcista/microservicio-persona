package com.bootcamp.persona.infrastructure.adapters.driving.webflux.exception;

import com.bootcamp.persona.domain.exception.BootcampServiceException;
import com.bootcamp.persona.domain.exception.EnrollmentConflictException;
import com.bootcamp.persona.domain.exception.InvalidPersonDataException;
import com.bootcamp.persona.infrastructure.adapters.driving.webflux.dto.ErrorResponse;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                          WebProperties webProperties,
                                          ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        setMessageWriters(serverCodecConfigurer.getWriters());
        setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        ErrorResponse response = toErrorResponse(getError(request));
        return ServerResponse.status(response.status())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }

    private ErrorResponse toErrorResponse(Throwable error) {
        if (error instanceof InvalidPersonDataException invalidData) {
            return response(HttpStatus.BAD_REQUEST, invalidData.getCode().getCode(),
                    invalidData.getMessage());
        }
        if (error instanceof EnrollmentConflictException conflict) {
            return response(HttpStatus.CONFLICT, conflict.getCode().getCode(),
                    conflict.getMessage());
        }
        if (error instanceof BootcampServiceException unavailable) {
            return response(HttpStatus.BAD_GATEWAY, unavailable.getCode().getCode(),
                    unavailable.getMessage());
        }
        if (error instanceof ServerWebInputException inputException) {
            return response(HttpStatus.BAD_REQUEST, "BAD_REQUEST",
                    inputException.getReason() == null ? "Entrada inválida" : inputException.getReason());
        }
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "Ocurrió un error inesperado");
    }

    private ErrorResponse response(HttpStatus status, String code, String message) {
        return new ErrorResponse(status.value(), code, message, Instant.now());
    }
}
