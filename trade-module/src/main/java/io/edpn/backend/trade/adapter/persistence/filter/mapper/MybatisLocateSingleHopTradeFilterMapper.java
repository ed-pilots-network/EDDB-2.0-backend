package io.edpn.backend.trade.adapter.persistence.filter.mapper;

import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;

public class MybatisLocateSingleHopTradeFilterMapper {
    public MybatisLocateSingleHopTradeFilter map(LocateSingleHopTradeFilter locateSingleHopTradeFilter) {
        return MybatisLocateSingleHopTradeFilter.builder()
                .xCoordinate(locateSingleHopTradeFilter.getXCoordinate())
                .yCoordinate(locateSingleHopTradeFilter.getYCoordinate())
                .zCoordinate(locateSingleHopTradeFilter.getZCoordinate())
                .commodityDisplayNames(locateSingleHopTradeFilter.getCommodityDisplayNames())
                .maxPriceAgeHours(locateSingleHopTradeFilter.getMaxPriceAgeHours())
                .maxRouteDistance(locateSingleHopTradeFilter.getMaxRouteDistance())
                .maxLandingPadSize(String.valueOf(locateSingleHopTradeFilter.getMaxLandingPadSize()))
                .maxArrivalDistance(locateSingleHopTradeFilter.getMaxArrivalDistance())
                .minSupply(locateSingleHopTradeFilter.getMinSupply())
                .minDemand(locateSingleHopTradeFilter.getMinDemand())
                .includeSurfaceStations(locateSingleHopTradeFilter.getIncludeSurfaceStations())
                .includeFleetCarriers(locateSingleHopTradeFilter.getIncludeFleetCarriers())
                .build();
    }
}
