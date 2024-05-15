package io.edpn.backend.trade.adapter.persistence.filter.mapper;

import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;

public class MybatisLocateSingleHopTradeFilterMapper {
    public MybatisLocateSingleHopTradeFilter map(LocateSingleHopTradeFilter locateSingleHopTradeFilter) {
        return MybatisLocateSingleHopTradeFilter.builder()
                .buyFromSystemName(locateSingleHopTradeFilter.getBuyFromSystemName())
                .buyFromStationName(locateSingleHopTradeFilter.getBuyFromStationName())
                .sellToSystemName(locateSingleHopTradeFilter.getSellToSystemName())
                .sellToStationName(locateSingleHopTradeFilter.getSellToStationName())
                .commodityDisplayNames(locateSingleHopTradeFilter.getCommodityDisplayNames())
                .maxPriceAgeHours(locateSingleHopTradeFilter.getMaxPriceAgeHours())
                .maxRouteDistance(locateSingleHopTradeFilter.getMaxRouteDistance())
                .maxLandingPadSize(String.valueOf(locateSingleHopTradeFilter.getMaxLandingPadSize()))
                .maxArrivalDistance(locateSingleHopTradeFilter.getMaxArrivalDistance())
                .minDemand(locateSingleHopTradeFilter.getMinDemand())
                .minSupply(locateSingleHopTradeFilter.getMinSupply())
                .includeSurfaceStations(locateSingleHopTradeFilter.getIncludeSurfaceStations())
                .includeFleetCarriers(locateSingleHopTradeFilter.getIncludeFleetCarriers())
                .build();
    }
}
