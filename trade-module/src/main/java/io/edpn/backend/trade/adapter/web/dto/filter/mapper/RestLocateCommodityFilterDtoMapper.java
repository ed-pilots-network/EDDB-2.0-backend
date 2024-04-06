package io.edpn.backend.trade.adapter.web.dto.filter.mapper;

import io.edpn.backend.trade.adapter.web.dto.filter.RestLocateCommodityFilterDto;
import io.edpn.backend.trade.application.domain.LandingPadSize;
import io.edpn.backend.trade.application.domain.filter.LocateCommodityFilter;

import java.util.Optional;

public class RestLocateCommodityFilterDtoMapper {
    public LocateCommodityFilter map(RestLocateCommodityFilterDto restLocateCommodityFilterDto) {
        return new LocateCommodityFilter(
                restLocateCommodityFilterDto.commodityDisplayName(),
                restLocateCommodityFilterDto.xCoordinate(),
                restLocateCommodityFilterDto.yCoordinate(),
                restLocateCommodityFilterDto.zCoordinate(),
                restLocateCommodityFilterDto.includePlanetary(),
                restLocateCommodityFilterDto.includeOdyssey(),
                restLocateCommodityFilterDto.includeFleetCarriers(),
                Optional.ofNullable(restLocateCommodityFilterDto.maxLandingPadSize()).map(LandingPadSize::valueOf).orElse(LandingPadSize.UNKNOWN),
                restLocateCommodityFilterDto.minSupply(),
                restLocateCommodityFilterDto.minDemand()
        );
    }
}
