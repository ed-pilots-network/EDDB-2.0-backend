package io.edpn.backend.trade.application.port.incomming.locatetraderoute;

import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;

import java.util.List;

public interface LocateSingleHopTradeRouteUseCase {
    
    List<SingleHopRoute> locateRoutesOrderByProfit(LocateSingleHopTradeFilter locateSingleHopTradeFilter);
}
