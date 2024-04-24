package io.edpn.backend.trade.application.port.outgoing.system;

import io.edpn.backend.trade.application.domain.System;

import java.util.List;

public interface LoadSystemsByNameContainingPort {
    
    List<System> loadSystemsByNameContaining(String name, Integer amount);
}
