package com.bootcamp.persona.application.config;

import com.bootcamp.persona.domain.api.PersonServicePort;
import com.bootcamp.persona.domain.spi.BootcampGatewayPort;
import com.bootcamp.persona.domain.spi.PersonPersistencePort;
import com.bootcamp.persona.domain.spi.ReportGatewayPort;
import com.bootcamp.persona.domain.usecase.PersonUseCase;
import com.bootcamp.persona.infrastructure.adapters.driven.http.BootcampGatewayAdapter;
import com.bootcamp.persona.infrastructure.adapters.driven.http.ReportGatewayAdapter;
import com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.adapter.PersonPersistenceAdapter;
import com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.mapper.PersonEntityMapper;
import com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.repository.PersonBootcampRepository;
import com.bootcamp.persona.infrastructure.adapters.driven.r2dbc.repository.PersonRepository;
import com.bootcamp.persona.infrastructure.adapters.driving.webflux.handler.PersonHandler;
import com.bootcamp.persona.infrastructure.adapters.driving.webflux.mapper.PersonDtoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfiguration {

    @Bean
    public PersonEntityMapper personEntityMapper() {
        return new PersonEntityMapper();
    }

    @Bean
    public PersonDtoMapper personDtoMapper() {
        return new PersonDtoMapper();
    }

    @Bean
    public PersonPersistencePort personPersistencePort(
            PersonRepository persons,
            PersonBootcampRepository enrollments,
            PersonEntityMapper mapper,
            TransactionalOperator transaction) {
        return new PersonPersistenceAdapter(persons, enrollments, mapper, transaction);
    }

    @Bean
    public BootcampGatewayPort bootcampGatewayPort(WebClient bootcampWebClient) {
        return new BootcampGatewayAdapter(bootcampWebClient);
    }

    @Bean
    public ReportGatewayPort reportGatewayPort(WebClient reportWebClient) {
        return new ReportGatewayAdapter(reportWebClient);
    }

    @Bean
    public PersonServicePort personServicePort(
            PersonPersistencePort persistence,
            BootcampGatewayPort bootcampGateway,
            ReportGatewayPort reportGateway) {
        return new PersonUseCase(persistence, bootcampGateway, reportGateway);
    }

    @Bean
    public PersonHandler personHandler(PersonServicePort service, PersonDtoMapper mapper) {
        return new PersonHandler(service, mapper);
    }
}
