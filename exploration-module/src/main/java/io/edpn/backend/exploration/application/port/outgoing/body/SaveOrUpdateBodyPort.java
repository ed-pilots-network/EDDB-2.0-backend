package io.edpn.backend.exploration.application.port.outgoing.body;

import io.edpn.backend.exploration.application.domain.Body;

public interface SaveOrUpdateBodyPort {

    Body saveOrUpdate(Body body);
}
