package io.edpn.backend.trade.application.port.outgoing.commodity;


import io.edpn.backend.trade.application.domain.Commodity;

public interface CreateCommodityPort {
    
    Commodity create(Commodity commodity);
}
