package io.edpn.backend.trade.adapter.web.dto.filter.mapper;

import io.edpn.backend.trade.adapter.web.dto.filter.RestLocateSingleHopRouteFilterDto;
import io.edpn.backend.trade.application.domain.LandingPadSize;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;

import java.util.ArrayList;
import java.util.Optional;

public class RestLocateSingleHopRouteFilterDtoMapper {
    
    public LocateSingleHopTradeFilter map(RestLocateSingleHopRouteFilterDto locateSingleHopRouteFilterDto) {
        
        return LocateSingleHopTradeFilter.builder()
                .xCoordinate(locateSingleHopRouteFilterDto.xCoordinate())
                .yCoordinate(locateSingleHopRouteFilterDto.yCoordinate())
                .zCoordinate(locateSingleHopRouteFilterDto.zCoordinate())
                .commodityDisplayNames(Optional.ofNullable(locateSingleHopRouteFilterDto.commodityDisplayNames()).orElseGet(ArrayList::new))
                .maxPriceAgeHours(locateSingleHopRouteFilterDto.maxPriceAgeHours())
                .maxRouteDistance(locateSingleHopRouteFilterDto.maxRouteDistance())
                .maxLandingPadSize(LandingPadSize.valueOf(locateSingleHopRouteFilterDto.maxLandingPadSize()))
                .maxArrivalDistance(locateSingleHopRouteFilterDto.maxArrivalDistance())
                .minSupply(locateSingleHopRouteFilterDto.minSupply())
                .minDemand(locateSingleHopRouteFilterDto.minDemand())
                .includeSurfaceStations(locateSingleHopRouteFilterDto.includeSurfaceStations())
                .includeFleetCarriers(locateSingleHopRouteFilterDto.includeFleetCarriers())
                .build();
    }
}
