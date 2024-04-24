package io.edpn.backend.trade.application.port.outgoing.station;

import io.edpn.backend.trade.application.domain.Station;

import java.util.List;

public interface LoadStationsBySystemNamePort {
    
    List<Station> findStationsBySystemName(String systemName);
}
