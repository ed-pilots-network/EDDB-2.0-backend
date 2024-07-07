package io.edpn.backend.trade.application.domain.filter;

import io.edpn.backend.trade.application.domain.LandingPadSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class LocateSingleHopTradeFilter {
    private String buyFromSystemName;
    private String buyFromStationName;
    private String sellToSystemName;
    private String sellToStationName;
    private List<String> commodityDisplayNames; //if empty default all
    private Integer maxPriceAgeHours; //max 72 (for now)
    private Integer maxRouteDistance; //default 80
    private LandingPadSize maxLandingPadSize;
    private Integer maxArrivalDistance; //default < 5000
    private Integer minSupply;
    private Integer minDemand;
    private Boolean includeSurfaceStations;
    private Boolean includeOdyssey;
    private Boolean includeFleetCarriers;

}
