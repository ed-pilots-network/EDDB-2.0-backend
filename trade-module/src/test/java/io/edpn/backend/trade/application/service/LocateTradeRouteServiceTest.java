package io.edpn.backend.trade.application.service;

import io.edpn.backend.trade.application.domain.LoopRoute;
import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateLoopTradeFilter;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateLoopTradeByFilterPort;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateSingleHopTradeByFilterPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocateTradeRouteServiceTest {
    
    @Mock
    private LocateSingleHopTradeByFilterPort locateSingleHopTradeByFilterPort;
    
    @Mock
    private LocateLoopTradeByFilterPort locateLoopTradeByFilterPort;
    
    private LocateTradeRouteService underTest;
    
    @BeforeEach
    public void setUp() {
        underTest = new LocateTradeRouteService(locateSingleHopTradeByFilterPort, locateLoopTradeByFilterPort);
    }
    
    @Test
    public void testLocateSingleHopRoutesOrderByProfit() {
        LocateSingleHopTradeFilter mockLocateSingleHopTradeFilter = mock(LocateSingleHopTradeFilter.class);
        SingleHopRoute mockSingleHopRoute = mock(SingleHopRoute.class);
        
        when(locateSingleHopTradeByFilterPort.locateRoutesByFilter(mockLocateSingleHopTradeFilter)).thenReturn(List.of(mockSingleHopRoute));
        
        List<SingleHopRoute> responseList = underTest.locateRoutesOrderByProfit(mockLocateSingleHopTradeFilter);
        
        assertThat(responseList, contains(mockSingleHopRoute));
    }
    
    @Test
    public void testLocateLoopRoutesOrderByProfit() {
        LocateLoopTradeFilter mockLocateLoopTradeFilter = mock(LocateLoopTradeFilter.class);
        LoopRoute mockLoopRoute = mock(LoopRoute.class);
        
        when(locateLoopTradeByFilterPort.locateRoutesByFilter(mockLocateLoopTradeFilter)).thenReturn(List.of(mockLoopRoute));
        
        List<LoopRoute> responseList = underTest.locateRoutesOrderByProfit(mockLocateLoopTradeFilter);
        
        assertThat(responseList, contains(mockLoopRoute));
    }
    
    
}