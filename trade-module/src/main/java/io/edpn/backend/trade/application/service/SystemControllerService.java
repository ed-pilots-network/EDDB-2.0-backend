package io.edpn.backend.trade.application.service;

import io.edpn.backend.trade.application.domain.System;
import io.edpn.backend.trade.application.domain.exception.ValidationException;
import io.edpn.backend.trade.application.port.incomming.system.FindSystemsByNameContainingUseCase;
import io.edpn.backend.trade.application.port.outgoing.system.LoadSystemsByNameContainingPort;
import io.edpn.backend.trade.application.validation.LoadByNameContainingValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class SystemControllerService implements FindSystemsByNameContainingUseCase {
    
    private final LoadSystemsByNameContainingPort loadSystemsByNameContainingPort;
    private final LoadByNameContainingValidator loadByNameContainingValidator;
    
    @Override
    public List<System> findSystemsByNameContaining(String subString, Integer amount) {
        Optional<ValidationException> validationResult = loadByNameContainingValidator.validate(subString, amount);
        if (validationResult.isPresent()) {
            throw validationResult.get();
        }
        
        return loadSystemsByNameContainingPort.loadSystemsByNameContaining(subString, amount);
    }
}
