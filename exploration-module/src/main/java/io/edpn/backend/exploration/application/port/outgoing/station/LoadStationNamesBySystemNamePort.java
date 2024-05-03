package io.edpn.backend.exploration.application.port.outgoing.station;

import java.util.List;

public interface LoadStationNamesBySystemNamePort {
    
    List<String> loadStationNamesBySystemName(String systemName);
}
