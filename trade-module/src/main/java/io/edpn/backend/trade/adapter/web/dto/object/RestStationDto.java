package io.edpn.backend.trade.adapter.web.dto.object;

import io.edpn.backend.trade.application.dto.web.object.StationDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Schema(name = "StationDto")
@Builder
@Jacksonized
public record RestStationDto(
        Long marketId,
        String name,
        Double arrivalDistance,
        RestSystemDto system,
        Boolean planetary,
        Boolean requireOdyssey,
        Boolean fleetCarrier,
        String maxLandingPadSize,
        LocalDateTime marketUpdatedAt) implements StationDto {
}
