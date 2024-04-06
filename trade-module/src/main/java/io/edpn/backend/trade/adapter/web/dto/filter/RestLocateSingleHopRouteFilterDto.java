package io.edpn.backend.trade.adapter.web.dto.filter;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RestLocateSingleHopRouteFilterDto(
        @NotNull Double xCoordinate,
        @NotNull Double yCoordinate,
        @NotNull Double zCoordinate,
        List<String> commodityDisplayNames,
        @NotNull Integer maxPriceAgeHours,
        @NotNull Integer maxRouteDistance,
        @NotNull String maxLandingPadSize,
        @NotNull Integer maxArrivalDistance,
        @NotNull Integer minSupply,
        @NotNull Integer minDemand,
        @NotNull Boolean includeSurfaceStations,
        @NotNull Boolean includeFleetCarriers) {

}
