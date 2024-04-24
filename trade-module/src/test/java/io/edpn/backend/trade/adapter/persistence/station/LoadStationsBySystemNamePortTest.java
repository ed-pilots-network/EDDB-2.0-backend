package io.edpn.backend.trade.adapter.persistence.station;

import io.edpn.backend.trade.adapter.persistence.StationRepository;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisStationEntity;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisStationEntityMapper;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisFindStationFilterMapper;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRepository;
import io.edpn.backend.trade.application.domain.Station;
import io.edpn.backend.trade.application.port.outgoing.station.LoadStationsBySystemNamePort;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoadStationsBySystemNamePortTest {
    
    @Mock
    private MybatisStationEntityMapper mybatisStationEntityMapper;
    
    @Mock
    private MybatisStationRepository mybatisStationRepository;
    
    @Mock
    private MybatisFindStationFilterMapper persistenceMybatisFindStationFilterMapper;
    
    private LoadStationsBySystemNamePort underTest;
    
    @BeforeEach
    void setUp() {
        underTest = new StationRepository(mybatisStationEntityMapper, mybatisStationRepository, persistenceMybatisFindStationFilterMapper);
    }
    
    @Test
    void testFindByFilter() {
        MybatisStationEntity MybatisStationEntity = mock(MybatisStationEntity.class);
        Station station = mock(Station.class);
        
        when(mybatisStationRepository.findStationsBySystemName("systemName")).thenReturn(List.of(MybatisStationEntity));
        when(mybatisStationEntityMapper.map(MybatisStationEntity)).thenReturn(station);
        
        List<Station> result = underTest.findStationsBySystemName("systemName");
        
        assertThat(result, Matchers.containsInAnyOrder(station));
    }
}
