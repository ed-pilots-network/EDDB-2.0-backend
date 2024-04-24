package io.edpn.backend.trade.application.service;

import io.edpn.backend.trade.application.domain.Station;
import io.edpn.backend.trade.application.port.incomming.station.FindStationsBySystemNameUseCase;
import io.edpn.backend.trade.application.port.outgoing.station.LoadStationsBySystemNamePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
public class FindStationsBySystemNameUseCaseTest {
    
    @Mock
    private LoadStationsBySystemNamePort loadStationsBySystemNamePort;
    
    private FindStationsBySystemNameUseCase underTest;
    
    @BeforeEach
    void setUp() {
        underTest = new StationControllerService(loadStationsBySystemNamePort);
    }
    
    @Test
    void testLoadStationsBySystemName() {
        String systemName = "systemName";
        Station station1 = mock(Station.class);
        Station station2 = mock(Station.class);
        when(loadStationsBySystemNamePort.findStationsBySystemName(systemName)).thenReturn(List.of(station1, station2));
        
        List<Station> result = underTest.findStationsBySystemName(systemName);
        
        assertThat(result, hasSize(2));
        assertThat(result, containsInAnyOrder(station1, station2));
    }
}

