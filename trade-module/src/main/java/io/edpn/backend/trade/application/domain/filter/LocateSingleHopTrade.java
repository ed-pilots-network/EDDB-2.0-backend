package io.edpn.backend.trade.application.domain.filter;

import io.edpn.backend.trade.application.domain.LandingPadSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class LocateSingleHopTrade {
    private UUID originSystem;
    private List<UUID> commodityIds; //if empty default all
    private String maxPriceAge; //either enum or add formats
    private Integer maxRouteDistance;
    private LandingPadSize maxLandingPadSize;
    private Integer arrivalDistance;
    private Integer minSupply;
    private Integer minDemand;
    private Boolean useSurfaceStations;
    private Boolean useFleetCarriers;
    
// Not needed?:
//              "government": "democracy",
//              "allegiance": "alliance",
//              "requiresPermit": false,
//              "includeOrbital": true,
}
