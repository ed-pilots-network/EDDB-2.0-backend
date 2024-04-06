package io.edpn.backend.trade.adapter.persistence.locatetraderoute;

import io.edpn.backend.trade.adapter.persistence.LocateTradeRouteRepository;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisSingleHopEntity;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisSingleHopEntityMapper;
import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateSingleHopTradeFilter;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisLocateSingleHopTradeFilterMapper;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateSingleHopTradeRouteRepository;
import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateSingleHopeTradeByFilterPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocateSingleHopeTradeByFilterPortTest {
    
    @Mock
    private MybatisLocateSingleHopTradeRouteRepository mybatisLocateSingleHopTradeRouteRepository;
    
    @Mock
    private MybatisSingleHopEntityMapper mybatisSingleHopEntityMapper;
    
    @Mock
    private MybatisLocateSingleHopTradeFilterMapper mybatisLocateSingleHopTradeFilterMapper;
    
    private LocateSingleHopeTradeByFilterPort underTest;
    
    @BeforeEach
    public void setup() {
        underTest = new LocateTradeRouteRepository(mybatisLocateSingleHopTradeRouteRepository, mybatisLocateSingleHopTradeFilterMapper, mybatisSingleHopEntityMapper);
    }
    
    @Test
    void testLocateSingleHopTradeByFilter() {
        //mock objects
        MybatisLocateSingleHopTradeFilter mybatisLocateSingleHopTradeFilter = mock(MybatisLocateSingleHopTradeFilter.class);
        MybatisSingleHopEntity mybatisSingleHopEntity = mock(MybatisSingleHopEntity.class);
        LocateSingleHopTradeFilter locateSingleHopTradeFilter = mock(LocateSingleHopTradeFilter.class);
        SingleHopRoute singleHopRoute = mock(SingleHopRoute.class);
        
        when(mybatisLocateSingleHopTradeFilterMapper.map(locateSingleHopTradeFilter)).thenReturn(mybatisLocateSingleHopTradeFilter);
        when(mybatisLocateSingleHopTradeRouteRepository.locateRoutesByFilter(mybatisLocateSingleHopTradeFilter)).thenReturn(Collections.singletonList(mybatisSingleHopEntity));
        when(mybatisSingleHopEntityMapper.map(mybatisSingleHopEntity)).thenReturn(singleHopRoute);
        
        List<SingleHopRoute> result = underTest.locateRoutesByFilter(locateSingleHopTradeFilter);
        
        verify(mybatisLocateSingleHopTradeFilterMapper).map(locateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository).locateRoutesByFilter(mybatisLocateSingleHopTradeFilter);
        verify(mybatisSingleHopEntityMapper).map(mybatisSingleHopEntity);
        verifyNoMoreInteractions(mybatisLocateSingleHopTradeFilterMapper, mybatisLocateSingleHopTradeRouteRepository, mybatisSingleHopEntityMapper);
        
        assertThat(result, equalTo(Collections.singletonList(singleHopRoute)));
        
    }
}
