package io.edpn.backend.trade.adapter.persistence;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisSingleHopEntity;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisSingleHopEntityMapper;
import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateSingleHopTradeFilter;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisLocateSingleHopTradeFilterMapper;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateSingleHopTradeRouteRepository;
import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateSingleHopTradeByFilterPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class
LocateTradeRouteRepository implements LocateSingleHopTradeByFilterPort {
    
    private final MybatisLocateSingleHopTradeRouteRepository locateSingleHopTradeRouteRepository;
    private final MybatisLocateSingleHopTradeFilterMapper locateSingleHopTradeFilterMapper;
    private final MybatisSingleHopEntityMapper singleHopEntityMapper;
    
    
    @Override
    public List<SingleHopRoute> locateRoutesByFilter(LocateSingleHopTradeFilter locateSingleHopTradeFilter) {
        MybatisLocateSingleHopTradeFilter mybatisLocateSingleHopTradeFilter = locateSingleHopTradeFilterMapper.map(locateSingleHopTradeFilter);
        
        
        boolean buySystemNameSet = Objects.nonNull(mybatisLocateSingleHopTradeFilter.getBuyFromSystemName());
        boolean sellSystemNameSet =  Objects.nonNull(mybatisLocateSingleHopTradeFilter.getSellToSystemName());
        boolean buyFromStationNameSet =  Objects.nonNull(mybatisLocateSingleHopTradeFilter.getBuyFromStationName());
        boolean sellToStationNameSet =  Objects.nonNull(mybatisLocateSingleHopTradeFilter.getSellToStationName());
        
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
    
    
}
