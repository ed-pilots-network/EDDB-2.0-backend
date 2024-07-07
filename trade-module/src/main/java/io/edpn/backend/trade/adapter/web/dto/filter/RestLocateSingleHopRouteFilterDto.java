package io.edpn.backend.trade.adapter.web.dto.filter;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RestLocateSingleHopRouteFilterDto(
        String buyFromSystemName,
        String buyFromStationName,
        String sellToSystemName,
        String sellToStationName,
        List<String> commodityDisplayNames,
        @NotNull Integer maxPriceAgeHours,
        @NotNull Integer maxRouteDistance,
        @NotNull String maxLandingPadSize,
        @NotNull Integer maxArrivalDistance,
        @NotNull Integer cargoCapacity,
        @NotNull Boolean includeSurfaceStations,
        @NotNull Boolean includeOdyssey,
        @NotNull Boolean includeFleetCarriers) {

}
