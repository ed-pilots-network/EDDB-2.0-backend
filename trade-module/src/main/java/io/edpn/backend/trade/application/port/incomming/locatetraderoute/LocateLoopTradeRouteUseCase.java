package io.edpn.backend.trade.application.port.incomming.locatetraderoute;

import io.edpn.backend.trade.application.domain.LoopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateLoopTradeFilter;

import java.util.List;

public interface LocateLoopTradeRouteUseCase {
    
    List<LoopRoute> locateRoutesOrderByProfit(LocateLoopTradeFilter locateLoopTradeFilter);
}
