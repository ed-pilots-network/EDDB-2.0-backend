package io.edpn.backend.trade.application.service;

import io.edpn.backend.trade.application.domain.LoopRoute;
import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateLoopTradeFilter;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.port.incomming.locatetraderoute.LocateLoopTradeRouteUseCase;
import io.edpn.backend.trade.application.port.incomming.locatetraderoute.LocateSingleHopTradeRouteUseCase;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateLoopTradeByFilterPort;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateSingleHopTradeByFilterPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LocateTradeRouteService implements LocateSingleHopTradeRouteUseCase, LocateLoopTradeRouteUseCase {
    
    private final LocateSingleHopTradeByFilterPort locateSingleHopTradeByFilterPort;
    private final LocateLoopTradeByFilterPort locateLoopTradeByFilterPort;
    
    @Override
    public List<SingleHopRoute> locateRoutesOrderByProfit(LocateSingleHopTradeFilter locateSingleHopTradeFilter) {
        return locateSingleHopTradeByFilterPort.locateRoutesByFilter(locateSingleHopTradeFilter);
    }
    
    @Override
    public List<LoopRoute> locateRoutesOrderByProfit(LocateLoopTradeFilter locateLoopTradeFilter) {
        return locateLoopTradeByFilterPort.locateRoutesByFilter(locateLoopTradeFilter);
    }
}
