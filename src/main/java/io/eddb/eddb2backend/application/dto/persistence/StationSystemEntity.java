package io.eddb.eddb2backend.application.dto.persistence;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationSystemEntity {

    private UUID stationId;
    private UUID systemId;

}



