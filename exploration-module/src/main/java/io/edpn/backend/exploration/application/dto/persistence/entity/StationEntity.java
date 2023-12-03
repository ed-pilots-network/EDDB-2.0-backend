package io.edpn.backend.exploration.application.dto.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface StationEntity {

    UUID getId();

    Long getMarketId();

    String getName();

    String getType();

    Double getDistanceFromStar();

    SystemEntity getSystem();

    Map<String, Integer> getLandingPads();

    String getGovernment();

    String getEconomy();

    Map<String, Double> getEconomies();

    List<String> getServices();

    Boolean getOdyssey();

    Boolean getHorizons();

    LocalDateTime getUpdatedAt();

}
