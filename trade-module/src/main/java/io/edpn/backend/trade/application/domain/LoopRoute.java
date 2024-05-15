package io.edpn.backend.trade.application.domain;

public record LoopRoute(
        SingleHopRoute firstTrip,
        SingleHopRoute returnTrip,
        Double distanceFromCommander) {
}
