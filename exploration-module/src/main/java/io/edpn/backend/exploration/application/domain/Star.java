package io.edpn.backend.exploration.application.domain;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Star(
        UUID id,
        Double absoluteMagnitude,
        Long age, // millions of years
        Double arrivalDistance, // Ls
        Double axialTilt,
        Boolean discovered,
        Long localId,
        String luminosity,
        Boolean mapped,
        String name,
        Double radius,
        List<Ring> rings,
        Double rotationalPeriod,
        String starType,
        Long stellarMass, // in multiples of Sol
        Integer subclass,
        Double surfaceTemperature,
        System system,
        Long systemAddress,
        Boolean horizons,
        Boolean odyssey,
        Double estimatedScanValue
) {
}