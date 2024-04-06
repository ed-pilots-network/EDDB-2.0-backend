package io.edpn.backend.trade.adapter.web.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "LocateSingeHopRouteDto")
@Builder
@Jacksonized
public record RestSingleHopRouteDto(
        RestValidatedCommodityDto commodityDto,
        RestStationDto buyFromStationDto,
        Long buyPrice,
        Long stock,
        RestStationDto sellToStationDto,
        Long sellPrice,
        Long demand,
        Long profit,
        Double routeDistance,
        Double distanceFromReferenceStation) {
}
