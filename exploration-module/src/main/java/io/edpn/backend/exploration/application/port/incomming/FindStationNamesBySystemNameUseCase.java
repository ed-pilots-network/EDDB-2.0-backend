package io.edpn.backend.exploration.application.port.incomming;

import java.util.List;

public interface FindStationNamesBySystemNameUseCase {
    
    List<String> findStationNamesBySystemName(String systemName);
}
