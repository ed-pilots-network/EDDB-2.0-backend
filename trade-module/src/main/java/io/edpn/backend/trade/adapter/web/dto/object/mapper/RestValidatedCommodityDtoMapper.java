package io.edpn.backend.trade.adapter.web.dto.object.mapper;

import io.edpn.backend.trade.adapter.web.dto.object.RestValidatedCommodityDto;
import io.edpn.backend.trade.application.domain.ValidatedCommodity;
import io.edpn.backend.trade.application.dto.web.object.ValidatedCommodityDto;
import io.edpn.backend.trade.application.dto.web.object.mapper.ValidatedCommodityDtoMapper;

public class RestValidatedCommodityDtoMapper implements ValidatedCommodityDtoMapper {
    @Override
    public ValidatedCommodityDto map(ValidatedCommodity validatedCommodity) {
        return new RestValidatedCommodityDto(
                validatedCommodity.commodityName(),
                validatedCommodity.displayName(),
                String.valueOf(validatedCommodity.type()),
                validatedCommodity.isRare()
        );
    }
}
