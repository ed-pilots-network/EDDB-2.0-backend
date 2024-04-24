package io.edpn.backend.trade.application.port.incomming.system;

import io.edpn.backend.trade.application.domain.System;

import java.util.List;

public interface FindSystemsByNameContainingUseCase {
    
    List<System> findSystemsByNameContaining(String subString, Integer amount);
}
