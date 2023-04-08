package io.eddb.eddb2backend.domain.model.system;

import io.eddb.eddb2backend.domain.model.Faction;
import lombok.Builder;

import java.sql.Date;

@Builder
//TODO: Investigate states. EDDB Data has it as an array of 3 objects for Happiness/Economy/Security
//TODO: Investigate minorFactionPresence array
public record System(Long id, String name, Coordinate coordinate, Long population, Government government,
                     Allegiance allegiance, Security security, Economy primaryEconomy, Power power,
                     PowerState powerState, boolean needsPermit, Date lastUpdated,
                     Faction controllingMinorFaction, ReserveType reserveType, Long edSystemAddress) {
}