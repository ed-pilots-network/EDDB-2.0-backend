package io.edpn.backend.trade.adapter.persistence.filter.mapper;

import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateLoopTradeFilter;
import io.edpn.backend.trade.application.domain.filter.LocateLoopTradeFilter;

public class MybatisLocateLoopTradeFilterMapper {
    
    public MybatisLocateLoopTradeFilter map(LocateLoopTradeFilter locateLoopTradeFilter) {
        return MybatisLocateLoopTradeFilter.builder()
                .xCoordinate(locateLoopTradeFilter.getXCoordinate())
                .yCoordinate(locateLoopTradeFilter.getYCoordinate())
                .zCoordinate(locateLoopTradeFilter.getZCoordinate())
                .commodityDisplayNames(locateLoopTradeFilter.getCommodityDisplayNames())
                .maxPriceAgeHours(locateLoopTradeFilter.getMaxPriceAgeHours())
                .maxRouteDistance(locateLoopTradeFilter.getMaxRouteDistance())
                .maxLandingPadSize(String.valueOf(locateLoopTradeFilter.getMaxLandingPadSize()))
                .maxArrivalDistance(locateLoopTradeFilter.getMaxArrivalDistance())
                .minSupply(locateLoopTradeFilter.getMinSupply())
                .minDemand(locateLoopTradeFilter.getMinDemand())
                .includeSurfaceStations(locateLoopTradeFilter.getIncludeSurfaceStations())
                .includeOdyssey(locateLoopTradeFilter.getIncludeOdyssey())
                .includeFleetCarriers(locateLoopTradeFilter.getIncludeFleetCarriers())
                .build();
    }
}
