package io.edpn.backend.exploration.application.port.outgoing.systemcoordinaterequest;

import io.edpn.backend.exploration.application.domain.SystemCoordinatesUpdatedEvent;
import org.springframework.context.event.EventListener;

public interface SystemCoordinatesUpdatedEventListener {

    @EventListener
    void onUpdatedEvent(SystemCoordinatesUpdatedEvent event);
}