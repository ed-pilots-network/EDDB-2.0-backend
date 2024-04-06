package io.edpn.backend.trade.adapter.persistence.entity.mapper;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisStationEntity;
import io.edpn.backend.trade.application.domain.LandingPadSize;
import io.edpn.backend.trade.application.domain.Station;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class MybatisStationEntityMapper {

    private final MybatisSystemEntityMapper mybatisSystemEntityMapper;
    private final MybatisMarketDatumEntityMapper mybatisMarketDatumEntityMapper;

    public Station map(MybatisStationEntity mybatisStationEntity) {
        return new Station(
                mybatisStationEntity.getId(),
                mybatisStationEntity.getMarketId(),
                mybatisStationEntity.getName(),
                mybatisStationEntity.getArrivalDistance(),
                mybatisSystemEntityMapper.map(mybatisStationEntity.getSystem()),
                mybatisStationEntity.getPlanetary(),
                mybatisStationEntity.getRequireOdyssey(),
                mybatisStationEntity.getFleetCarrier(),
                LandingPadSize.fromInteger(mybatisStationEntity.getMaxLandingPadSize()),
                mybatisStationEntity.getMarketUpdatedAt(),
                mybatisStationEntity.getMarketData().stream().map(mybatisMarketDatumEntityMapper::map).toList()
        );
    }

    public MybatisStationEntity map(Station station) {
        return MybatisStationEntity.builder()
                .id(station.id())
                .marketId(station.marketId())
                .name(station.name())
                .arrivalDistance(station.arrivalDistance())
                .system(mybatisSystemEntityMapper.map(station.system()))
                .planetary(station.planetary())
                .requireOdyssey(station.requireOdyssey())
                .fleetCarrier(station.fleetCarrier())
                .maxLandingPadSize(Optional.ofNullable(station.maxLandingPadSize()).map(LandingPadSize::value).orElse(LandingPadSize.UNKNOWN.value()))
                .marketUpdatedAt(station.marketUpdatedAt())
                .marketData(station.marketData().stream().map(mybatisMarketDatumEntityMapper::map).toList())
                .build();
    }
}
