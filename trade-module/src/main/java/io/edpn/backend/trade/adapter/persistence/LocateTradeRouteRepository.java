package io.edpn.backend.trade.adapter.persistence;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisLoopTradeEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisSingleHopEntity;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisLoopTradeEntityMapper;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisSingleHopEntityMapper;
import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateLoopTradeFilter;
import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateSingleHopTradeFilter;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisLocateLoopTradeFilterMapper;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisLocateSingleHopTradeFilterMapper;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateLoopTradeRouteRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateSingleHopTradeRouteRepository;
import io.edpn.backend.trade.application.domain.LoopRoute;
import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateLoopTradeFilter;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateLoopTradeByFilterPort;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateSingleHopTradeByFilterPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class
LocateTradeRouteRepository implements LocateSingleHopTradeByFilterPort, LocateLoopTradeByFilterPort {
    
    private final MybatisLocateSingleHopTradeRouteRepository locateSingleHopTradeRouteRepository;
    private final MybatisLocateSingleHopTradeFilterMapper locateSingleHopTradeFilterMapper;
    private final MybatisSingleHopEntityMapper singleHopEntityMapper;
    
    private final MybatisLocateLoopTradeRouteRepository locateLoopTradeRouteRepository;
    private final MybatisLocateLoopTradeFilterMapper locateLoopTradeFilterMapper;
    private final MybatisLoopTradeEntityMapper loopTradeEntityMapper;
    
    
    @Override
    public List<SingleHopRoute> locateRoutesByFilter(LocateSingleHopTradeFilter locateSingleHopTradeFilter) {
        MybatisLocateSingleHopTradeFilter mybatisLocateSingleHopTradeFilter = locateSingleHopTradeFilterMapper.map(locateSingleHopTradeFilter);
        
        
        boolean buySystemNameSet = Objects.nonNull(mybatisLocateSingleHopTradeFilter.getBuyFromSystemName());
        boolean sellSystemNameSet = Objects.nonNull(mybatisLocateSingleHopTradeFilter.getSellToSystemName());
        boolean buyFromStationNameSet = Objects.nonNull(mybatisLocateSingleHopTradeFilter.getBuyFromStationName());
        boolean sellToStationNameSet = Objects.nonNull(mybatisLocateSingleHopTradeFilter.getSellToStationName());
        
        List<MybatisSingleHopEntity> results = null;
        
        if (!buySystemNameSet && sellSystemNameSet && !sellToStationNameSet) {  // buySystem not set, buyStation should bet ignored, sellSystem set, sellStation not set
            results = locateSingleHopTradeRouteRepository.findBestBuyWithinRangeOfSystem(mybatisLocateSingleHopTradeFilter);
        }
        if (!buySystemNameSet && sellSystemNameSet && sellToStationNameSet) { // buy system not set, buyStation should bet ignored, sellSystem set, sell station set
            results = locateSingleHopTradeRouteRepository.findBestBuyWithinRangeOfStation(mybatisLocateSingleHopTradeFilter);
        }
        if (buySystemNameSet && !buyFromStationNameSet && !sellSystemNameSet) { // buy system set, buyStation not set, sellSystem not set, sellStation should bet ignored
            results = locateSingleHopTradeRouteRepository.findBestSellWithinRangeOfSystem(mybatisLocateSingleHopTradeFilter);
        }
        if (buySystemNameSet && buyFromStationNameSet && !sellSystemNameSet) { // buy system set, buyStation set, sellSystem not set, sellStation should bet ignored
            results = locateSingleHopTradeRouteRepository.findBestSellWithinRangeOfStation(mybatisLocateSingleHopTradeFilter);
        }
        if (buySystemNameSet && !buyFromStationNameSet && sellSystemNameSet && !sellToStationNameSet) { // buy system set, buyStation not set, sellSystem set, sellStation not set
            results = locateSingleHopTradeRouteRepository.findBestTradeBetweenSystems(mybatisLocateSingleHopTradeFilter);
        }
        if (buySystemNameSet && buyFromStationNameSet && sellSystemNameSet && sellToStationNameSet) { // buy system set, buyStation set, sellSystem set, sellStation set
            results = locateSingleHopTradeRouteRepository.findBestTradeBetweenStations(mybatisLocateSingleHopTradeFilter);
        }
        
        if (Objects.isNull(results)) {
            throw new IllegalArgumentException("No valid combination of input parameters was found");
        }
        
        return results.stream()
                .map(singleHopEntityMapper::map)
                .toList();
        
    }
    
    
    @Override
    public List<LoopRoute> locateRoutesByFilter(LocateLoopTradeFilter locateLoopTradeFilter) {
        MybatisLocateLoopTradeFilter mybatisLocateLoopTradeFilter = locateLoopTradeFilterMapper.map(locateLoopTradeFilter);
        List<MybatisLoopTradeEntity> bestOneWayTrades = locateLoopTradeRouteRepository.locateFirstHalfLoop(mybatisLocateLoopTradeFilter);
        
        if (bestOneWayTrades == null) {
            return new ArrayList<>();
        }
        
        bestOneWayTrades.forEach(
                trade -> {
                    MybatisLocateSingleHopTradeFilter singleHopFilter = map(mybatisLocateLoopTradeFilter, trade.getFirstTripEntity());
                    List<MybatisSingleHopEntity> returnTrip = locateSingleHopTradeRouteRepository.findBestTradeBetweenStations(singleHopFilter);
                    trade.setReturnTripEntity(!returnTrip.isEmpty() ? returnTrip.getFirst() : null);
                });
        
        return bestOneWayTrades.stream()
                .filter(trade -> trade.getReturnTripEntity() != null)
                .sorted(Comparator.comparing( (MybatisLoopTradeEntity trade) ->
                        trade.getFirstTripEntity().getProfit() +
                        trade.getReturnTripEntity().getProfit()).reversed()
                )
                .map(loopTradeEntityMapper::map)
                .toList();
    }
    
    private MybatisLocateSingleHopTradeFilter map(MybatisLocateLoopTradeFilter filter, MybatisSingleHopEntity firstTrip) {
        
        return MybatisLocateSingleHopTradeFilter.builder()
                .buyFromSystemName(firstTrip.getSellToStationEntity().getSystem().getName())
                .buyFromStationName(firstTrip.getSellToStationEntity().getName())
                .sellToSystemName(firstTrip.getBuyFromStationEntity().getSystem().getName())
                .sellToStationName(firstTrip.getBuyFromStationEntity().getName())
                .commodityDisplayNames(filter.getCommodityDisplayNames())
                .maxPriceAgeHours(filter.getMaxPriceAgeHours())
                .maxRouteDistance(filter.getMaxRouteDistance())
                .maxLandingPadSize(filter.getMaxLandingPadSize())
                .maxArrivalDistance(filter.getMaxArrivalDistance())
                .minSupply(filter.getMinSupply())
                .minDemand(filter.getMinDemand())
                .includeSurfaceStations(filter.getIncludeSurfaceStations())
                .includeFleetCarriers(filter.getIncludeFleetCarriers())
                .build();
    }
}
