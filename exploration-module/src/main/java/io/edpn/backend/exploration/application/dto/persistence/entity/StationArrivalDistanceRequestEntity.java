package io.edpn.backend.exploration.application.dto.persistence.entity;

import io.edpn.backend.util.Module;

public interface StationArrivalDistanceRequestEntity {
    
    String getSystemName();
    
    String getStationName();
    
    Module getRequestingModule();
}
