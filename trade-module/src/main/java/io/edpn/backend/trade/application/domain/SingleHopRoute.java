package io.edpn.backend.trade.application.domain;

public record SingleHopRoute(
        ValidatedCommodity commodity,
        Station buyFromStation,
        Long buyPrice,
        Long stock,
        Station sellToStation,
        Long sellPrice,
        Long demand,
        Long profit,
        Double routeDistance) {
}
