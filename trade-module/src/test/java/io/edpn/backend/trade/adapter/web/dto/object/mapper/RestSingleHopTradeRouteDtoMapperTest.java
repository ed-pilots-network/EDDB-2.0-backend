package io.edpn.backend.trade.adapter.web.dto.object.mapper;

import io.edpn.backend.trade.adapter.web.dto.object.RestSingleHopRouteDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestStationDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestValidatedCommodityDto;
import io.edpn.backend.trade.application.domain.SingleHopRoute;
import io.edpn.backend.trade.application.domain.Station;
import io.edpn.backend.trade.application.domain.ValidatedCommodity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestSingleHopTradeRouteDtoMapperTest {
    
    @Mock
    private RestStationDtoMapper stationDtoMapper;
    
    @Mock
    private RestValidatedCommodityDtoMapper validatedCommodityDtoMapper;
    
    private RestSingleHopRouteDtoMapper underTest;
    
    @BeforeEach
    public void setUp() {
        underTest = new RestSingleHopRouteDtoMapper(validatedCommodityDtoMapper, stationDtoMapper);
    }
    
    @Test
    public void testMap_givenDomainObject_shouldReturnDto() {
        RestValidatedCommodityDto mockCommodityDto = mock(RestValidatedCommodityDto.class);
        RestStationDto mockBuyFromStationDto = mock(RestStationDto.class);
        RestStationDto mockSellToStationDto = mock(RestStationDto.class);
        
        SingleHopRoute domainObject = new SingleHopRoute(
                mock(ValidatedCommodity.class),
                mock(Station.class),
                100L,
                200L,
                mock(Station.class),
                300L,
                400L,
                500L,
                60.0
        );
        
        when(validatedCommodityDtoMapper.map(domainObject.commodity())).thenReturn(mockCommodityDto);
        when(stationDtoMapper.map(domainObject.buyFromStation())).thenReturn(mockBuyFromStationDto);
        when(stationDtoMapper.map(domainObject.sellToStation())).thenReturn(mockSellToStationDto);
        
        RestSingleHopRouteDto result = underTest.map(domainObject);
        
        assertThat(result.commodityDto(), is(mockCommodityDto));
        assertThat(result.buyFromStationDto(), is(mockBuyFromStationDto));
        assertThat(result.buyPrice(), is(100L));
        assertThat(result.stock(), is(200L));
        assertThat(result.sellToStationDto(), is(mockSellToStationDto));
        assertThat(result.sellPrice(), is(300L));
        assertThat(result.demand(), is(400L));
        assertThat(result.profit(), is(500L));
        assertThat(result.routeDistance(), is(60.0));
        
        verify(validatedCommodityDtoMapper, times(1)).map(domainObject.commodity());
        verify(stationDtoMapper, times(1)).map(domainObject.buyFromStation());
        verify(stationDtoMapper, times(1)).map(domainObject.sellToStation());
    }
}
