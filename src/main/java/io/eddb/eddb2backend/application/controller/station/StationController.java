package io.eddb.eddb2backend.application.controller.station;

import io.eddb.eddb2backend.application.dto.station.GetStationResponse;
import io.eddb.eddb2backend.application.mapper.StationMapper;
import io.eddb.eddb2backend.application.usecase.GetStationUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {

    private final GetStationUsecase getStationUsecase;

    @GetMapping("/{id}")
    public ResponseEntity<GetStationResponse> getStation(@PathVariable Long id) {
        return getStationUsecase.getById(id)
                .map(StationMapper::map)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Collection<GetStationResponse>> getAllStations() {
        var responseList = getStationUsecase.getAll()
                .stream()
                .map(StationMapper::map)
                .toList();
        return ResponseEntity.ok(responseList);
    }
}
