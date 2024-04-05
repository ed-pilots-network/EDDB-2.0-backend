package io.edpn.backend.trade.application.port.outgoing.locatetraderoute;

import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;

import java.util.List;

public interface LocateSingleHopeTradeByFilterPort {
    
    List<SingleHopRoute> locateRoutesByFilter(LocateSingleHopTradeFilter locateSingleHopTradeFilter);
}
