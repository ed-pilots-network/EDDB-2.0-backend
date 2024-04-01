package io.edpn.backend.exploration.adapter.persistence.entity.mapper;

import io.edpn.backend.exploration.adapter.persistence.entity.MybatisStationEntity;
import io.edpn.backend.exploration.application.domain.LandingPadSize;
import io.edpn.backend.exploration.application.domain.Station;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MybatisStationEntityMapper {

    private final MybatisSystemEntityMapper systemEntityMapper;

    private static Map<String, Integer> getLandingPads(Station station) {
        return Optional.ofNullable(station.landingPads())
                .flatMap(MybatisStationEntityMapper::filterLandingPads)
                .orElse(null);
    }

    private static Optional<Map<String, Integer>> filterLandingPads(Map<LandingPadSize, Integer> landingPads) {
        Map<String, Integer> filteredLandingPadsMap = landingPads.entrySet().stream()
                .filter(entry -> LandingPadSize.KNOWN_LANDING_PAD_SIZES.contains(entry.getKey()))
                .collect(Collectors.toMap(entry -> entry.getKey().name(), Map.Entry::getValue));
        return filteredLandingPadsMap.isEmpty() ? Optional.empty() : Optional.of(filteredLandingPadsMap);
    }

    private static Map<LandingPadSize, Integer> getLandingPads(MybatisStationEntity stationEntity) {
        if (stationEntity.getLandingPads() == null) {
            return new HashMap<>();
        }

        return stationEntity.getLandingPads().entrySet().stream()
                .collect(Collectors.toMap(
                        k -> LandingPadSize.fromString(k.getKey()),
                        Map.Entry::getValue,
                        (first, second) -> first));
    }

    public Station map(MybatisStationEntity stationEntity) {
        return new Station(
                stationEntity.getId(),
                stationEntity.getMarketId(),
                stationEntity.getName(),
                stationEntity.getType(),
                stationEntity.getDistanceFromStar(),
                Optional.ofNullable(stationEntity.getSystem()).map(systemEntityMapper::map).orElse(null),
                getLandingPads(stationEntity),
                stationEntity.getEconomies(),
                stationEntity.getServices(),
                stationEntity.getEconomy(),
                stationEntity.getGovernment(),
                stationEntity.getOdyssey(),
                stationEntity.getHorizons(),
                stationEntity.getUpdatedAt()
        );
    }

    public MybatisStationEntity map(Station station) {
        return MybatisStationEntity.builder()
                .id(station.id())
                .marketId(station.marketId())
                .name(station.name())
                .type(station.type())
                .distanceFromStar(station.distanceFromStar())
                .system(Optional.ofNullable(station.system()).map(systemEntityMapper::map).orElse(null))
                .landingPads(getLandingPads(station))
                .economies(station.economies())
                .services(station.services())
                .economy(station.economy())
                .government(station.government())
                .odyssey(station.odyssey())
                .horizons(station.horizons())
                .updatedAt(station.updatedAt())
                .build();
    }
}
