package io.edpn.backend.trade.application.service;

import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.port.incomming.locatetraderoute.LocateSingleHopTradeRouteUseCase;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateSingleHopTradeByFilterPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LocateTradeRouteService implements LocateSingleHopTradeRouteUseCase {
    
    private final LocateSingleHopTradeByFilterPort locateSingleHopTradeByFilterPort;
    
    @Override
    public List<SingleHopRoute> locateRoutesOrderByProfit(LocateSingleHopTradeFilter locateSingleHopTradeFilter) {
        return locateSingleHopTradeByFilterPort.locateRoutesByFilter(locateSingleHopTradeFilter);
    }
}
