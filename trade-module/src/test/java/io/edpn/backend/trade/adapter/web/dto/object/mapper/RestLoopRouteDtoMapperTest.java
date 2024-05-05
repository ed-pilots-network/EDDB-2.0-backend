package io.edpn.backend.trade.adapter.web.dto.object.mapper;

import io.edpn.backend.trade.adapter.web.dto.object.RestLoopRouteDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestSingleHopRouteDto;
import io.edpn.backend.trade.application.domain.LoopRoute;
import io.edpn.backend.trade.application.domain.SingleHopRoute;
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
public class RestLoopRouteDtoMapperTest {
    
    @Mock
    private RestSingleHopRouteDtoMapper restSingleHopRouteDtoMapper;
    
    private RestLoopRouteDtoMapper underTest;
    
    @BeforeEach
    public void setUp() {
        underTest = new RestLoopRouteDtoMapper(restSingleHopRouteDtoMapper);
    }
    
    @Test
    public void testMap_givenDomainObject_shouldReturnDto() {
        RestSingleHopRouteDto mockFirstTripDto = mock(RestSingleHopRouteDto.class);
        RestSingleHopRouteDto mockReturnTripDto = mock(RestSingleHopRouteDto.class);
        
        LoopRoute domainObject = new LoopRoute(
                mock(SingleHopRoute.class),
                mock(SingleHopRoute.class),
                100.0
        );
        
        when(restSingleHopRouteDtoMapper.map(domainObject.firstTrip())).thenReturn(mockFirstTripDto);
        when(restSingleHopRouteDtoMapper.map(domainObject.returnTrip())).thenReturn(mockReturnTripDto);
        
        RestLoopRouteDto result = underTest.map(domainObject);
        
        assertThat(result.firstTrip(), is(mockFirstTripDto));
        assertThat(result.returnTrip(), is(mockReturnTripDto));
        assertThat(result.distanceFromCommander(), is(100.0));
        
        verify(restSingleHopRouteDtoMapper, times(1)).map(domainObject.firstTrip());
        verify(restSingleHopRouteDtoMapper, times(1)).map(domainObject.returnTrip());
    }
}
