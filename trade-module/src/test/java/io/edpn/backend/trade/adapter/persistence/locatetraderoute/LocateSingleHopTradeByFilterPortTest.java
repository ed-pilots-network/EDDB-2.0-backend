package io.edpn.backend.trade.adapter.persistence.locatetraderoute;

import io.edpn.backend.trade.adapter.persistence.LocateTradeRouteRepository;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisSingleHopEntity;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisSingleHopEntityMapper;
import io.edpn.backend.trade.adapter.persistence.filter.MybatisLocateSingleHopTradeFilter;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisLocateSingleHopTradeFilterMapper;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateSingleHopTradeRouteRepository;
import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateSingleHopTradeByFilterPort;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocateSingleHopTradeByFilterPortTest {
    
    @Mock
    private MybatisLocateSingleHopTradeRouteRepository mybatisLocateSingleHopTradeRouteRepository;
    
    @Mock
    private MybatisSingleHopEntityMapper mybatisSingleHopEntityMapper;
    
    @Mock
    private MybatisLocateSingleHopTradeFilterMapper mybatisLocateSingleHopTradeFilterMapper;
    
    private LocateSingleHopTradeByFilterPort underTest;
    
    @BeforeEach
    public void setup() {
        underTest = new LocateTradeRouteRepository(mybatisLocateSingleHopTradeRouteRepository, mybatisLocateSingleHopTradeFilterMapper, mybatisSingleHopEntityMapper);
    }
    
    @Test
    void testLocateSingleHopTradeByFilter_findBestBuyWithinRangeOfSystem(){
        MybatisLocateSingleHopTradeFilter mybatisLocateSingleHopTradeFilter = MybatisLocateSingleHopTradeFilter.builder()
                .sellToSystemName("SellToSystem")
                .commodityDisplayNames(List.of("Display", "Names"))
                .maxPriceAgeHours(72)
                .maxRouteDistance(80)
                .maxLandingPadSize("SMALL")
                .maxArrivalDistance(5000)
                .cargoCapacity(720)
                .includeSurfaceStations(true)
                .includeFleetCarriers(false)
                .build();
        MybatisSingleHopEntity mybatisSingleHopEntity = mock(MybatisSingleHopEntity.class);
        
        LocateSingleHopTradeFilter locateSingleHopTradeFilter = mock(LocateSingleHopTradeFilter.class);
        SingleHopRoute singleHopRoute = mock(SingleHopRoute.class);
        
        when(mybatisLocateSingleHopTradeFilterMapper.map(locateSingleHopTradeFilter)).thenReturn(mybatisLocateSingleHopTradeFilter);
        when(mybatisLocateSingleHopTradeRouteRepository.findBestBuyWithinRangeOfSystem(mybatisLocateSingleHopTradeFilter)).thenReturn(Collections.singletonList(mybatisSingleHopEntity));
        when(mybatisSingleHopEntityMapper.map(mybatisSingleHopEntity)).thenReturn(singleHopRoute);
        
        List<SingleHopRoute> result = underTest.locateRoutesByFilter(locateSingleHopTradeFilter);
        
        verify(mybatisLocateSingleHopTradeFilterMapper).map(locateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository).findBestBuyWithinRangeOfSystem(mybatisLocateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository, times(1)).findBestBuyWithinRangeOfSystem(mybatisLocateSingleHopTradeFilter);
        verify(mybatisSingleHopEntityMapper).map(mybatisSingleHopEntity);
        verifyNoMoreInteractions(mybatisLocateSingleHopTradeFilterMapper, mybatisLocateSingleHopTradeRouteRepository, mybatisSingleHopEntityMapper);
        
        assertThat(result, equalTo(Collections.singletonList(singleHopRoute)));
    }
    
    @Test
    void testLocateSingleHopTradeByFilter_findBestBuyWithinRangeOfStation(){
        MybatisLocateSingleHopTradeFilter mybatisLocateSingleHopTradeFilter = MybatisLocateSingleHopTradeFilter.builder()
                .sellToSystemName("SellToSystem")
                .sellToStationName("SellToStation")
                .commodityDisplayNames(List.of("Display", "Names"))
                .maxPriceAgeHours(72)
                .maxRouteDistance(80)
                .maxLandingPadSize("SMALL")
                .maxArrivalDistance(5000)
                .cargoCapacity(720)
                .includeSurfaceStations(true)
                .includeFleetCarriers(false)
                .build();
        MybatisSingleHopEntity mybatisSingleHopEntity = mock(MybatisSingleHopEntity.class);
        
        LocateSingleHopTradeFilter locateSingleHopTradeFilter = mock(LocateSingleHopTradeFilter.class);
        SingleHopRoute singleHopRoute = mock(SingleHopRoute.class);
        
        when(mybatisLocateSingleHopTradeFilterMapper.map(locateSingleHopTradeFilter)).thenReturn(mybatisLocateSingleHopTradeFilter);
        when(mybatisLocateSingleHopTradeRouteRepository.findBestBuyWithinRangeOfStation(mybatisLocateSingleHopTradeFilter)).thenReturn(Collections.singletonList(mybatisSingleHopEntity));
        when(mybatisSingleHopEntityMapper.map(mybatisSingleHopEntity)).thenReturn(singleHopRoute);
        
        List<SingleHopRoute> result = underTest.locateRoutesByFilter(locateSingleHopTradeFilter);
        
        verify(mybatisLocateSingleHopTradeFilterMapper).map(locateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository).findBestBuyWithinRangeOfStation(mybatisLocateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository, times(1)).findBestBuyWithinRangeOfStation(mybatisLocateSingleHopTradeFilter);
        verify(mybatisSingleHopEntityMapper).map(mybatisSingleHopEntity);
        verifyNoMoreInteractions(mybatisLocateSingleHopTradeFilterMapper, mybatisLocateSingleHopTradeRouteRepository, mybatisSingleHopEntityMapper);
        
        assertThat(result, equalTo(Collections.singletonList(singleHopRoute)));
    }
    
    @Test
    void testLocateSingleHopTradeByFilter_findBestSellWithinRangeOfSystem(){
        MybatisLocateSingleHopTradeFilter mybatisLocateSingleHopTradeFilter = MybatisLocateSingleHopTradeFilter.builder()
                .buyFromSystemName("BuyFromSystem")
                .commodityDisplayNames(List.of("Display", "Names"))
                .maxPriceAgeHours(72)
                .maxRouteDistance(80)
                .maxLandingPadSize("SMALL")
                .maxArrivalDistance(5000)
                .cargoCapacity(720)
                .includeSurfaceStations(true)
                .includeFleetCarriers(false)
                .build();
        MybatisSingleHopEntity mybatisSingleHopEntity = mock(MybatisSingleHopEntity.class);
        
        LocateSingleHopTradeFilter locateSingleHopTradeFilter = mock(LocateSingleHopTradeFilter.class);
        SingleHopRoute singleHopRoute = mock(SingleHopRoute.class);
        
        when(mybatisLocateSingleHopTradeFilterMapper.map(locateSingleHopTradeFilter)).thenReturn(mybatisLocateSingleHopTradeFilter);
        when(mybatisLocateSingleHopTradeRouteRepository.findBestSellWithinRangeOfSystem(mybatisLocateSingleHopTradeFilter)).thenReturn(Collections.singletonList(mybatisSingleHopEntity));
        when(mybatisSingleHopEntityMapper.map(mybatisSingleHopEntity)).thenReturn(singleHopRoute);
        
        List<SingleHopRoute> result = underTest.locateRoutesByFilter(locateSingleHopTradeFilter);
        
        verify(mybatisLocateSingleHopTradeFilterMapper).map(locateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository).findBestSellWithinRangeOfSystem(mybatisLocateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository, times(1)).findBestSellWithinRangeOfSystem(mybatisLocateSingleHopTradeFilter);
        verify(mybatisSingleHopEntityMapper).map(mybatisSingleHopEntity);
        verifyNoMoreInteractions(mybatisLocateSingleHopTradeFilterMapper, mybatisLocateSingleHopTradeRouteRepository, mybatisSingleHopEntityMapper);
        
        assertThat(result, equalTo(Collections.singletonList(singleHopRoute)));
    }
    
    @Test
    void testLocateSingleHopTradeByFilter_findBestSellWithinRangeOfStation(){
        MybatisLocateSingleHopTradeFilter mybatisLocateSingleHopTradeFilter = MybatisLocateSingleHopTradeFilter.builder()
                .buyFromSystemName("BuyFromSystem")
                .buyFromStationName("BuyFromStation")
                .commodityDisplayNames(List.of("Display", "Names"))
                .maxPriceAgeHours(72)
                .maxRouteDistance(80)
                .maxLandingPadSize("SMALL")
                .maxArrivalDistance(5000)
                .cargoCapacity(720)
                .includeSurfaceStations(true)
                .includeFleetCarriers(false)
                .build();
        MybatisSingleHopEntity mybatisSingleHopEntity = mock(MybatisSingleHopEntity.class);
        
        LocateSingleHopTradeFilter locateSingleHopTradeFilter = mock(LocateSingleHopTradeFilter.class);
        SingleHopRoute singleHopRoute = mock(SingleHopRoute.class);
        
        when(mybatisLocateSingleHopTradeFilterMapper.map(locateSingleHopTradeFilter)).thenReturn(mybatisLocateSingleHopTradeFilter);
        when(mybatisLocateSingleHopTradeRouteRepository.findBestSellWithinRangeOfStation(mybatisLocateSingleHopTradeFilter)).thenReturn(Collections.singletonList(mybatisSingleHopEntity));
        when(mybatisSingleHopEntityMapper.map(mybatisSingleHopEntity)).thenReturn(singleHopRoute);
        
        List<SingleHopRoute> result = underTest.locateRoutesByFilter(locateSingleHopTradeFilter);
        
        verify(mybatisLocateSingleHopTradeFilterMapper).map(locateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository).findBestSellWithinRangeOfStation(mybatisLocateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository, times(1)).findBestSellWithinRangeOfStation(mybatisLocateSingleHopTradeFilter);
        verify(mybatisSingleHopEntityMapper).map(mybatisSingleHopEntity);
        verifyNoMoreInteractions(mybatisLocateSingleHopTradeFilterMapper, mybatisLocateSingleHopTradeRouteRepository, mybatisSingleHopEntityMapper);
        
        assertThat(result, equalTo(Collections.singletonList(singleHopRoute)));
    }
    
    @Test
    void testLocateSingleHopTradeByFilter_findBestTradeBetweenSystems(){
        MybatisLocateSingleHopTradeFilter mybatisLocateSingleHopTradeFilter = MybatisLocateSingleHopTradeFilter.builder()
                .buyFromSystemName("BuyFromSystem")
                .sellToSystemName("SellToSystem")
                .commodityDisplayNames(List.of("Display", "Names"))
                .maxPriceAgeHours(72)
                .maxRouteDistance(80)
                .maxLandingPadSize("SMALL")
                .maxArrivalDistance(5000)
                .cargoCapacity(720)
                .includeSurfaceStations(true)
                .includeFleetCarriers(false)
                .build();
        MybatisSingleHopEntity mybatisSingleHopEntity = mock(MybatisSingleHopEntity.class);
        
        LocateSingleHopTradeFilter locateSingleHopTradeFilter = mock(LocateSingleHopTradeFilter.class);
        SingleHopRoute singleHopRoute = mock(SingleHopRoute.class);
        
        when(mybatisLocateSingleHopTradeFilterMapper.map(locateSingleHopTradeFilter)).thenReturn(mybatisLocateSingleHopTradeFilter);
        when(mybatisLocateSingleHopTradeRouteRepository.findBestTradeBetweenSystems(mybatisLocateSingleHopTradeFilter)).thenReturn(Collections.singletonList(mybatisSingleHopEntity));
        when(mybatisSingleHopEntityMapper.map(mybatisSingleHopEntity)).thenReturn(singleHopRoute);
        
        List<SingleHopRoute> result = underTest.locateRoutesByFilter(locateSingleHopTradeFilter);
        
        verify(mybatisLocateSingleHopTradeFilterMapper).map(locateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository).findBestTradeBetweenSystems(mybatisLocateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository, times(1)).findBestTradeBetweenSystems(mybatisLocateSingleHopTradeFilter);
        verify(mybatisSingleHopEntityMapper).map(mybatisSingleHopEntity);
        verifyNoMoreInteractions(mybatisLocateSingleHopTradeFilterMapper, mybatisLocateSingleHopTradeRouteRepository, mybatisSingleHopEntityMapper);
        
        assertThat(result, equalTo(Collections.singletonList(singleHopRoute)));
    }
    
    @Test
    void testLocateSingleHopTradeByFilter_findBestTradeBetweenStations(){
        MybatisLocateSingleHopTradeFilter mybatisLocateSingleHopTradeFilter = MybatisLocateSingleHopTradeFilter.builder()
                .buyFromSystemName("BuyFromSystem")
                .buyFromStationName("BuyFromStation")
                .sellToSystemName("SellToSystem")
                .sellToStationName("SellToStation")
                .commodityDisplayNames(List.of("Display", "Names"))
                .maxPriceAgeHours(72)
                .maxRouteDistance(80)
                .maxLandingPadSize("SMALL")
                .maxArrivalDistance(5000)
                .cargoCapacity(720)
                .includeSurfaceStations(true)
                .includeFleetCarriers(false)
                .build();
        MybatisSingleHopEntity mybatisSingleHopEntity = mock(MybatisSingleHopEntity.class);
        
        LocateSingleHopTradeFilter locateSingleHopTradeFilter = mock(LocateSingleHopTradeFilter.class);
        SingleHopRoute singleHopRoute = mock(SingleHopRoute.class);
        
        when(mybatisLocateSingleHopTradeFilterMapper.map(locateSingleHopTradeFilter)).thenReturn(mybatisLocateSingleHopTradeFilter);
        when(mybatisLocateSingleHopTradeRouteRepository.findBestTradeBetweenStations(mybatisLocateSingleHopTradeFilter)).thenReturn(Collections.singletonList(mybatisSingleHopEntity));
        when(mybatisSingleHopEntityMapper.map(mybatisSingleHopEntity)).thenReturn(singleHopRoute);
        
        List<SingleHopRoute> result = underTest.locateRoutesByFilter(locateSingleHopTradeFilter);
        
        verify(mybatisLocateSingleHopTradeFilterMapper).map(locateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository).findBestTradeBetweenStations(mybatisLocateSingleHopTradeFilter);
        verify(mybatisLocateSingleHopTradeRouteRepository, times(1)).findBestTradeBetweenStations(mybatisLocateSingleHopTradeFilter);
        verify(mybatisSingleHopEntityMapper).map(mybatisSingleHopEntity);
        verifyNoMoreInteractions(mybatisLocateSingleHopTradeFilterMapper, mybatisLocateSingleHopTradeRouteRepository, mybatisSingleHopEntityMapper);
        
        assertThat(result, equalTo(Collections.singletonList(singleHopRoute)));
    }
}
