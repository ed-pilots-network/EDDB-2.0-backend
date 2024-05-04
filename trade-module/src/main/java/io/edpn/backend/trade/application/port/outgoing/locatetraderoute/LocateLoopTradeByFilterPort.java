package io.edpn.backend.trade.application.port.outgoing.locatetraderoute;

import io.edpn.backend.trade.application.domain.LoopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateLoopTradeFilter;

import java.util.List;

public interface LocateLoopTradeByFilterPort {
    
    List<LoopRoute> locateRoutesByFilter(LocateLoopTradeFilter locateLoopTradeFilter);
}
