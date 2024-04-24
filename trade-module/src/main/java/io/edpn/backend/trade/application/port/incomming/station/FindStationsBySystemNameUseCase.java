package io.edpn.backend.trade.application.port.incomming.station;

import io.edpn.backend.trade.application.domain.Station;

import java.util.List;

public interface FindStationsBySystemNameUseCase {
    
    List<Station> findStationsBySystemName(String systemName);
}
