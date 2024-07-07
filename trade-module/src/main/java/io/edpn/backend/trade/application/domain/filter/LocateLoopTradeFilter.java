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
public class LocateLoopTradeFilter {
    private Double xCoordinate;
    private Double yCoordinate;
    private Double zCoordinate;
    private List<String> commodityDisplayNames;
    private Integer maxPriceAgeHours;
    private Integer maxRouteDistance;
    private LandingPadSize maxLandingPadSize;
    private Integer maxArrivalDistance;
    private Integer minSupply;
    private Integer minDemand;
    private Boolean includeSurfaceStations;
    private Boolean includeOdyssey;
    private Boolean includeFleetCarriers;
}
