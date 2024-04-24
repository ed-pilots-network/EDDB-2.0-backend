package io.edpn.backend.trade.application.service;

import io.edpn.backend.trade.application.domain.Station;
import io.edpn.backend.trade.application.port.incomming.station.FindStationsBySystemNameUseCase;
import io.edpn.backend.trade.application.port.outgoing.station.LoadStationsBySystemNamePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class StationControllerService implements FindStationsBySystemNameUseCase {
    
    private final LoadStationsBySystemNamePort loadStationsBySystemNamePort;
    
    @Override
    public List<Station> findStationsBySystemName(String systemName) {
        
        return loadStationsBySystemNamePort.findStationsBySystemName(systemName);
    }
}
