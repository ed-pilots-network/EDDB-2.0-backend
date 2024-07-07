package io.edpn.backend.trade.adapter.web.dto.filter.mapper;

import io.edpn.backend.trade.adapter.web.dto.filter.RestLocateSingleHopRouteFilterDto;
import io.edpn.backend.trade.application.domain.LandingPadSize;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;

import java.util.ArrayList;
import java.util.Optional;

public class RestLocateSingleHopRouteFilterDtoMapper {
    
    public LocateSingleHopTradeFilter map(RestLocateSingleHopRouteFilterDto locateSingleHopRouteFilterDto) {
        
        return LocateSingleHopTradeFilter.builder()
                .buyFromSystemName(locateSingleHopRouteFilterDto.buyFromSystemName())
                .buyFromStationName(locateSingleHopRouteFilterDto.buyFromStationName())
                .sellToSystemName(locateSingleHopRouteFilterDto.sellToSystemName())
                .sellToStationName(locateSingleHopRouteFilterDto.sellToStationName())
                .commodityDisplayNames(Optional.ofNullable(locateSingleHopRouteFilterDto.commodityDisplayNames()).orElseGet(ArrayList::new))
                .maxPriceAgeHours(locateSingleHopRouteFilterDto.maxPriceAgeHours())
                .maxRouteDistance(locateSingleHopRouteFilterDto.maxRouteDistance())
                .maxLandingPadSize(LandingPadSize.valueOf(locateSingleHopRouteFilterDto.maxLandingPadSize()))
                .maxArrivalDistance(locateSingleHopRouteFilterDto.maxArrivalDistance())
                .minDemand(locateSingleHopRouteFilterDto.cargoCapacity() * 4) // no loss on sale
                .minSupply(locateSingleHopRouteFilterDto.cargoCapacity()) // enough supply for full cargo hold
                .includeSurfaceStations(locateSingleHopRouteFilterDto.includeSurfaceStations())
                .includeOdyssey(locateSingleHopRouteFilterDto.includeOdyssey())
                .includeFleetCarriers(locateSingleHopRouteFilterDto.includeFleetCarriers())
                .build();
    }
}
