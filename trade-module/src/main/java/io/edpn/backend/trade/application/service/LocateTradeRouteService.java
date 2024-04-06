package io.edpn.backend.trade.application.service;

import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.port.incomming.locatetraderoute.LocateSingleHopTradeRouteUseCase;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateSingleHopeTradeByFilterPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LocateTradeRouteService implements LocateSingleHopTradeRouteUseCase {
    
    private final LocateSingleHopeTradeByFilterPort locateSingleHopeTradeByFilterPort;
    
    @Override
    public List<SingleHopRoute> locateRoutesOrderByProfit(LocateSingleHopTradeFilter locateSingleHopTradeFilter) {
        return locateSingleHopeTradeByFilterPort.locateRoutesByFilter(locateSingleHopTradeFilter);
    }
}
