package io.edpn.backend.exploration.application.service;

import io.edpn.backend.exploration.application.domain.System;
import io.edpn.backend.exploration.application.domain.exception.ValidationException;
import io.edpn.backend.exploration.application.port.incomming.FindStationNamesBySystemNameUseCase;
import io.edpn.backend.exploration.application.port.incomming.FindSystemsByNameContainingUseCase;
import io.edpn.backend.exploration.application.port.outgoing.station.LoadStationNamesBySystemNamePort;
import io.edpn.backend.exploration.application.port.outgoing.system.LoadSystemsByNameContainingPort;
import io.edpn.backend.exploration.application.validation.LoadByNameContainingValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class SystemControllerService implements FindSystemsByNameContainingUseCase, FindStationNamesBySystemNameUseCase {

    private final LoadSystemsByNameContainingPort loadSystemsByNameContainingPort;
    private final LoadByNameContainingValidator loadByNameContainingValidator;
    private final LoadStationNamesBySystemNamePort loadStationNamesBySystemNamePort;
    
    @Override
    public List<System> findSystemsByNameContaining(String subString, int amount) {
        Optional<ValidationException> validationResult = loadByNameContainingValidator.validate(subString, amount);
        if (validationResult.isPresent()) {
            throw validationResult.get();
        }

        return loadSystemsByNameContainingPort.loadByNameContaining(subString, amount);
    }
    
    @Override
    public List<String> findStationNamesBySystemName(String systemName) {
        return loadStationNamesBySystemNamePort.loadStationNamesBySystemName(systemName);
    }
}
