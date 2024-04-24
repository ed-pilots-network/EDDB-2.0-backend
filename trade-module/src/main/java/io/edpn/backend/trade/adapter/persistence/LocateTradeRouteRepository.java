package io.edpn.backend.trade.adapter.persistence;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisSingleHopEntity;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisSingleHopEntityMapper;
import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateSingleHopTradeFilter;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisLocateSingleHopTradeFilterMapper;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateSingleHopTradeRouteRepository;
import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateSingleHopeTradeByFilterPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class LocateTradeRouteRepository implements LocateSingleHopeTradeByFilterPort {
    
    private final MybatisLocateSingleHopTradeRouteRepository locateSingleHopTradeRouteRepository;
    private final MybatisLocateSingleHopTradeFilterMapper locateSingleHopTradeFilterMapper;
    private final MybatisSingleHopEntityMapper singleHopEntityMapper;
    
    
    @Override
    public List<SingleHopRoute> locateRoutesByFilter(LocateSingleHopTradeFilter locateSingleHopTradeFilter) {
        MybatisLocateSingleHopTradeFilter mybatisLocateSingleHopTradeFilter = locateSingleHopTradeFilterMapper.map(locateSingleHopTradeFilter);
        
        int caseNumber = (mybatisLocateSingleHopTradeFilter.getBuyFromSystemName() == null ? 0 : 4)
                + (mybatisLocateSingleHopTradeFilter.getSellToSystemName() == null ? 0 : 2)
                + (mybatisLocateSingleHopTradeFilter.getSellToStationName() == null && mybatisLocateSingleHopTradeFilter.getBuyFromStationName() == null ? 0 : 1);
        
        
        List<MybatisSingleHopEntity> results = new ArrayList<>();
        
        switch (caseNumber) {
             // BuySystem is null, SellSystem is defined but SellStation is null
            case 2 ->  results.addAll(locateSingleHopTradeRouteRepository.findBestBuyWithinRangeOfSystem(mybatisLocateSingleHopTradeFilter));
             
             // BuySystem is null, SellSystem and SellStation are defined
            case 3 ->  results.addAll(locateSingleHopTradeRouteRepository.findBestBuyWithinRangeOfStation(mybatisLocateSingleHopTradeFilter));
            
             // SellSystem is null, BuySystem is defined but BuyStation is null
            case 4 ->  results.addAll(locateSingleHopTradeRouteRepository.findBestSellWithinRangeOfSystem(mybatisLocateSingleHopTradeFilter));
            
            // SellSystem is null, BuySystem and BuyStation are defined
            case 5 ->  results.addAll(locateSingleHopTradeRouteRepository.findBestSellWithinRangeOfStation(mybatisLocateSingleHopTradeFilter));

            // Both systems are defined but stations are null
            case 6 ->  results.addAll(locateSingleHopTradeRouteRepository.findBestTradeBetweenSystems(mybatisLocateSingleHopTradeFilter));

            // Both systems and both stations are defined
            case 7 ->  results.addAll(locateSingleHopTradeRouteRepository.findBestTradeBetweenStations(mybatisLocateSingleHopTradeFilter));
            
            // Handle the case for 0 and any other unexpected case
            default -> throw new RuntimeException("Error");
        }
        
        return results.stream()
                .map(singleHopEntityMapper::map)
                .toList();
        
    }
    
    
}
