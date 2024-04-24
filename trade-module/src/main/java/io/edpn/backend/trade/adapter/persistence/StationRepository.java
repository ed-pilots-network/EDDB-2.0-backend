package io.edpn.backend.trade.adapter.persistence;

import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisStationEntityMapper;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisFindStationFilterMapper;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRepository;
import io.edpn.backend.trade.application.domain.Station;
import io.edpn.backend.trade.application.domain.filter.FindStationFilter;
import io.edpn.backend.trade.application.port.outgoing.station.CreateOrLoadStationPort;
import io.edpn.backend.trade.application.port.outgoing.station.LoadStationByIdPort;
import io.edpn.backend.trade.application.port.outgoing.station.LoadStationsByFilterPort;
import io.edpn.backend.trade.application.port.outgoing.station.LoadStationsBySystemNamePort;
import io.edpn.backend.trade.application.port.outgoing.station.UpdateStationPort;
import io.edpn.backend.util.exception.DatabaseEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class StationRepository implements CreateOrLoadStationPort, LoadStationByIdPort, UpdateStationPort, LoadStationsByFilterPort, LoadStationsBySystemNamePort {

    private final MybatisStationEntityMapper mybatisStationEntityMapper;
    private final MybatisStationRepository mybatisStationRepository;
    private final MybatisFindStationFilterMapper mybatisFindStationFilterMapper;

    @Override
    public Station createOrLoad(Station station) {
        return mybatisStationEntityMapper.map(mybatisStationRepository.createOrUpdateOnConflict(mybatisStationEntityMapper.map(station)));
    }

    @Override
    public Optional<Station> loadById(UUID uuid) {
        return mybatisStationRepository.findById(uuid)
                .map(mybatisStationEntityMapper::map);
    }

    @Override
    public Station update(Station station) {
        var entity = mybatisStationEntityMapper.map(station);
        mybatisStationRepository.update(entity);

        return loadById(entity.getId())
                .orElseThrow(() -> new DatabaseEntityNotFoundException("station with id: " + entity.getId() + " could not be found after update"));
    }

    @Override
    public List<Station> loadByFilter(FindStationFilter findStationFilter) {
        return mybatisStationRepository.findByFilter(mybatisFindStationFilterMapper.map(findStationFilter))
                .stream()
                .map(mybatisStationEntityMapper::map)
                .toList();
    }
    
    @Override
    public List<Station> findStationsBySystemName(String systemName) {
        return mybatisStationRepository.findStationsBySystemName(systemName)
                .stream()
                .map(mybatisStationEntityMapper::map)
                .toList();
    }
}
