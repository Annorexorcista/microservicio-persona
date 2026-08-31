package com.bootcamp.persona.domain.spi;

import com.bootcamp.persona.domain.model.BootcampInfo;
import reactor.core.publisher.Mono;

public interface BootcampGatewayPort {
    Mono<BootcampInfo> findById(Long id);
}
