package io.edpn.backend.exploration.adapter.persistence.entity.mapper;

import io.edpn.backend.exploration.adapter.persistence.entity.MybatisStationEntity;
import io.edpn.backend.exploration.adapter.persistence.entity.MybatisSystemEntity;
import io.edpn.backend.exploration.application.domain.LandingPadSize;
import io.edpn.backend.exploration.application.domain.Station;
import io.edpn.backend.exploration.application.domain.System;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MybatisStationEntityMapperTest {

    @Mock
    private MybatisSystemEntityMapper systemEntityMapper;
    private MybatisStationEntityMapper underTest;

    @BeforeEach
    public void setUp() {
        underTest = new MybatisStationEntityMapper(systemEntityMapper);
    }

    @Test
    public void testMap_givenStationEntity_shouldReturnStation() {
        UUID stationId = UUID.randomUUID();
        MybatisSystemEntity systemEntity = mock(MybatisSystemEntity.class);
        System system = mock(System.class);
        when(systemEntityMapper.map(any(MybatisSystemEntity.class))).thenReturn(system);
        Map<LandingPadSize, Integer> expectedLandingPadSizes = Map.of(
                LandingPadSize.LARGE, 1,
                LandingPadSize.MEDIUM, 2,
                LandingPadSize.SMALL, 3
        );

        MybatisStationEntity stationEntity = new MybatisStationEntity(
                stationId,
                123L,
                "stationName",
                "Corriolis",
                1000.0,
                systemEntity,
                Map.of(
                        "LARGE", 1,
                        "MEDIUM", 2,
                        "SMALL", 3
                ),
                Map.of(
                        "economy1", 0.1,
                        "economy2", 0.9
                ),
                "economy2",
                List.of("service1", "service2"),
                "government",
                true,
                true,
                LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        );

        Station result = underTest.map(stationEntity);

        assertThat(result.id(), equalTo(stationEntity.getId()));
        assertThat(result.marketId(), equalTo(stationEntity.getMarketId()));
        assertThat(result.name(), equalTo(stationEntity.getName()));
        assertThat(result.type(), equalTo(stationEntity.getType()));
        assertThat(result.distanceFromStar(), equalTo(stationEntity.getDistanceFromStar()));
        assertThat(result.system(), equalTo(system));
        assertThat(result.landingPads(), equalTo(expectedLandingPadSizes));
        assertThat(result.economies(), equalTo(stationEntity.getEconomies()));
        assertThat(result.services(), equalTo(stationEntity.getServices()));
        assertThat(result.economy(), equalTo(stationEntity.getEconomy()));
        assertThat(result.government(), equalTo(stationEntity.getGovernment()));
        assertThat(result.odyssey(), equalTo(stationEntity.getOdyssey()));
        assertThat(result.horizons(), equalTo(stationEntity.getHorizons()));
        assertThat(result.updatedAt(), equalTo(stationEntity.getUpdatedAt()));
    }

    @Test
    public void testMap_givenStation_shouldReturnStationEntity() {
        UUID stationId = UUID.randomUUID();
        System system = mock(System.class);
        MybatisSystemEntity systemEntity = mock(MybatisSystemEntity.class);
        when(systemEntityMapper.map(any(System.class))).thenReturn(systemEntity);
        Map<String, Integer> expectedLandingPadSizes = Map.of(
                "LARGE", 1,
                "MEDIUM", 2,
                "SMALL", 3
        );

        Station station = new Station(
                stationId,
                123L,
                "stationName",
                "Corriolis",
                1000.0,
                system,
                Map.of(
                        LandingPadSize.LARGE, 1,
                        LandingPadSize.MEDIUM, 2,
                        LandingPadSize.SMALL, 3
                ),
                Map.of(
                        "economy1", 0.1,
                        "economy2", 0.9
                ),
                List.of("service1", "service2"),
                "economy2",
                "government",
                true,
                true,
                LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        );


        MybatisStationEntity result = underTest.map(station);


        assertThat(result.getId(), equalTo(station.id()));
        assertThat(result.getMarketId(), equalTo(station.marketId()));
        assertThat(result.getName(), equalTo(station.name()));
        assertThat(result.getType(), equalTo(station.type()));
        assertThat(result.getDistanceFromStar(), equalTo(station.distanceFromStar()));
        assertThat(result.getSystem(), equalTo(systemEntity));
        assertThat(result.getLandingPads(), equalTo(expectedLandingPadSizes));
        assertThat(result.getEconomies(), equalTo(station.economies()));
        assertThat(result.getServices(), equalTo(station.services()));
        assertThat(result.getEconomy(), equalTo(station.economy()));
        assertThat(result.getGovernment(), equalTo(station.government()));
        assertThat(result.getOdyssey(), equalTo(station.odyssey()));
        assertThat(result.getHorizons(), equalTo(station.horizons()));
        assertThat(result.getUpdatedAt(), equalTo(station.updatedAt()));
    }
    @Test
    public void testMap_givenStation_withEmptyLandingPads() {
        UUID stationId = UUID.randomUUID();
        System system = mock(System.class);
        MybatisSystemEntity systemEntity = mock(MybatisSystemEntity.class);
        when(systemEntityMapper.map(any(System.class))).thenReturn(systemEntity);
        Map<String, Integer> expectedLandingPadSizes = null;

        Station station = new Station(
                stationId,
                123L,
                "stationName",
                "Corriolis",
                1000.0,
                system,
                Map.of(),
                Map.of(
                        "economy1", 0.1,
                        "economy2", 0.9
                ),
                List.of("service1", "service2"),
                "economy2",
                "government",
                true,
                true,
                LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        );


        MybatisStationEntity result = underTest.map(station);


        assertThat(result.getId(), equalTo(station.id()));
        assertThat(result.getMarketId(), equalTo(station.marketId()));
        assertThat(result.getName(), equalTo(station.name()));
        assertThat(result.getType(), equalTo(station.type()));
        assertThat(result.getDistanceFromStar(), equalTo(station.distanceFromStar()));
        assertThat(result.getSystem(), equalTo(systemEntity));
        assertThat(result.getLandingPads(), equalTo(expectedLandingPadSizes));
        assertThat(result.getEconomies(), equalTo(station.economies()));
        assertThat(result.getServices(), equalTo(station.services()));
        assertThat(result.getEconomy(), equalTo(station.economy()));
        assertThat(result.getGovernment(), equalTo(station.government()));
        assertThat(result.getOdyssey(), equalTo(station.odyssey()));
        assertThat(result.getHorizons(), equalTo(station.horizons()));
        assertThat(result.getUpdatedAt(), equalTo(station.updatedAt()));
    }

    @Test
    public void testMap_givenStation_withNullLandingPads() {
        UUID stationId = UUID.randomUUID();
        System system = mock(System.class);
        MybatisSystemEntity systemEntity = mock(MybatisSystemEntity.class);
        when(systemEntityMapper.map(any(System.class))).thenReturn(systemEntity);
        Map<String, Integer> expectedLandingPadSizes = null;

        Station station = new Station(
                stationId,
                123L,
                "stationName",
                "Corriolis",
                1000.0,
                system,
                null,
                Map.of(
                        "economy1", 0.1,
                        "economy2", 0.9
                ),
                List.of("service1", "service2"),
                "economy2",
                "government",
                true,
                true,
                LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        );


        MybatisStationEntity result = underTest.map(station);


        assertThat(result.getId(), equalTo(station.id()));
        assertThat(result.getMarketId(), equalTo(station.marketId()));
        assertThat(result.getName(), equalTo(station.name()));
        assertThat(result.getType(), equalTo(station.type()));
        assertThat(result.getDistanceFromStar(), equalTo(station.distanceFromStar()));
        assertThat(result.getSystem(), equalTo(systemEntity));
        assertThat(result.getLandingPads(), equalTo(expectedLandingPadSizes));
        assertThat(result.getEconomies(), equalTo(station.economies()));
        assertThat(result.getServices(), equalTo(station.services()));
        assertThat(result.getEconomy(), equalTo(station.economy()));
        assertThat(result.getGovernment(), equalTo(station.government()));
        assertThat(result.getOdyssey(), equalTo(station.odyssey()));
        assertThat(result.getHorizons(), equalTo(station.horizons()));
        assertThat(result.getUpdatedAt(), equalTo(station.updatedAt()));
    }
}