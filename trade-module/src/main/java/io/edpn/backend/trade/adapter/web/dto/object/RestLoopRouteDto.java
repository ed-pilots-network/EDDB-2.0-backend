package io.edpn.backend.trade.adapter.web.dto.object;

public record RestLoopRouteDto(
        RestSingleHopRouteDto firstTrip,
        RestSingleHopRouteDto returnTrip,
        Double distanceFromCommander) {
}
