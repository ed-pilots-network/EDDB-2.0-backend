package io.edpn.backend.trade.adapter.persistence;

import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisSingleHopEntityMapper;
import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateSingleHopTradeFilter;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisLocateSingleHopTradeFilterMapper;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateSingleHopTradeRouteRepository;
import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateSingleHopeTradeByFilterPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LocateTradeRouteRepository implements LocateSingleHopeTradeByFilterPort {
    
    private final MybatisLocateSingleHopTradeRouteRepository locateSingleHopTradeRouteRepository;
    private final MybatisLocateSingleHopTradeFilterMapper locateSingleHopTradeFilterMapper;
    private final MybatisSingleHopEntityMapper singleHopEntityMapper;
    
    
    @Override
    public List<SingleHopRoute> locateRoutesByFilter(LocateSingleHopTradeFilter locateSingleHopTradeFilter) {
        MybatisLocateSingleHopTradeFilter mybatisLocateSingleHopTradeFilter = locateSingleHopTradeFilterMapper.map(locateSingleHopTradeFilter);
        
        return locateSingleHopTradeRouteRepository
                .locateRoutesByFilter(mybatisLocateSingleHopTradeFilter)
                .stream()
                .map(singleHopEntityMapper::map)
                .toList();
    }
}
