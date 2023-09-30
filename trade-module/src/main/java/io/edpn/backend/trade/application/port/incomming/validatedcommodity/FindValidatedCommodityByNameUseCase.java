package io.edpn.backend.trade.application.port.incomming.validatedcommodity;

import io.edpn.backend.trade.application.dto.web.object.ValidatedCommodityDto;

import java.util.Optional;

public interface FindValidatedCommodityByNameUseCase {
    
    Optional<ValidatedCommodityDto> findByName(String displayName);
}
