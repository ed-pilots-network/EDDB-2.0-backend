package io.edpn.backend.exploration.application.service;

import io.edpn.backend.exploration.application.port.incomming.FindStationNamesBySystemNameUseCase;
import io.edpn.backend.exploration.application.port.outgoing.station.LoadStationNamesBySystemNamePort;
import io.edpn.backend.exploration.application.port.outgoing.system.LoadSystemsByNameContainingPort;
import io.edpn.backend.exploration.application.validation.LoadByNameContainingValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindStationsNamesBySystemNameUseCaseTest {
    
    @Mock
    private LoadSystemsByNameContainingPort loadSystemsByNameContainingPort;
    @Mock
    private LoadByNameContainingValidator loadByNameContainingValidator;
    @Mock
    private LoadStationNamesBySystemNamePort loadStationNamesBySystemNamePort;
    
    private FindStationNamesBySystemNameUseCase underTest;
    
    @BeforeEach
    void setUp() {
        underTest = new SystemControllerService(loadSystemsByNameContainingPort, loadByNameContainingValidator, loadStationNamesBySystemNamePort);
    }
    
    @Test
    void testFindSystemsByNameContaining() {
        String systemName = "Sol";
        
        List<String> stations = List.of(
                "Columbus",
                "Daedalus",
                "Dekker's Yard",
                "Dewsnap Prospecting Station",
                "Fraser Industrial Moulding",
                "Galileo",
                "Ji's Slumber",
                "Li Qing Jao",
                "M.Gorbachev",
                "Majoro Entertainment Complex",
                "Mars High");
        
        when(loadStationNamesBySystemNamePort.loadStationNamesBySystemName(systemName)).thenReturn(stations);
        
        List<String> result = underTest.findStationNamesBySystemName(systemName);
        
        assertThat(result, hasSize(11));
        assertThat(result, containsInAnyOrder(
                "Columbus",
                "Daedalus",
                "Dekker's Yard",
                "Dewsnap Prospecting Station",
                "Fraser Industrial Moulding",
                "Galileo",
                "Ji's Slumber",
                "Li Qing Jao",
                "M.Gorbachev",
                "Majoro Entertainment Complex",
                "Mars High"));
    }
}
