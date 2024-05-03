package io.edpn.backend.exploration.adapter.persistence;


import io.edpn.backend.exploration.adapter.persistence.entity.mapper.MybatisStationEntityMapper;
import io.edpn.backend.exploration.application.domain.Station;
import io.edpn.backend.exploration.application.port.outgoing.station.LoadStationNamesBySystemNamePort;
import io.edpn.backend.exploration.application.port.outgoing.station.LoadStationPort;
import io.edpn.backend.exploration.application.port.outgoing.station.SaveOrUpdateStationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class StationRepository implements SaveOrUpdateStationPort, LoadStationPort, LoadStationNamesBySystemNamePort {

    private final MybatisStationRepository mybatisStationRepository;
    private final MybatisStationEntityMapper stationEntityMapper;

    @Override
    public Station saveOrUpdate(Station station) {
        return stationEntityMapper.map(mybatisStationRepository.insertOrUpdateOnConflict(stationEntityMapper.map(station)));
    }

    @Override
    public Optional<Station> load(String systemName, String name) {
        return mybatisStationRepository.findByIdentifier(systemName, name)
                .map(stationEntityMapper::map);
    }
    
    @Override
    public List<String> loadStationNamesBySystemName(String systemName) {
        return mybatisStationRepository.findStationNamesBySystemName(systemName);
    }
}
