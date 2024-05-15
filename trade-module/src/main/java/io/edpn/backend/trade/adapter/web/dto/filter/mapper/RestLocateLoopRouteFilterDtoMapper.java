package io.edpn.backend.trade.adapter.web.dto.filter.mapper;

import io.edpn.backend.trade.adapter.web.dto.filter.RestLocateLoopRouteFilterDto;
import io.edpn.backend.trade.application.domain.LandingPadSize;
import io.edpn.backend.trade.application.domain.filter.LocateLoopTradeFilter;

import java.util.ArrayList;
import java.util.Optional;

public class RestLocateLoopRouteFilterDtoMapper {
    
    public LocateLoopTradeFilter map(RestLocateLoopRouteFilterDto locateLoopRouteFilterDto){
        
        return LocateLoopTradeFilter.builder()
                .xCoordinate(locateLoopRouteFilterDto.xCoordinate())
                .yCoordinate(locateLoopRouteFilterDto.yCoordinate())
                .zCoordinate(locateLoopRouteFilterDto.zCoordinate())
                .commodityDisplayNames(Optional.ofNullable(locateLoopRouteFilterDto.commodityDisplayNames()).orElseGet(ArrayList::new))
                .maxPriceAgeHours(locateLoopRouteFilterDto.maxPriceAgeHours())
                .maxRouteDistance(locateLoopRouteFilterDto.maxRouteDistance())
                .maxLandingPadSize(LandingPadSize.valueOf(locateLoopRouteFilterDto.maxLandingPadSize()))
                .maxArrivalDistance(locateLoopRouteFilterDto.maxArrivalDistance())
                .minSupply(locateLoopRouteFilterDto.minSupply())
                .minDemand(locateLoopRouteFilterDto.minDemand())
                .includeSurfaceStations(locateLoopRouteFilterDto.includeSurfaceStations())
                .includeFleetCarriers(locateLoopRouteFilterDto.includeFleetCarriers())
                .build();
    }
}
