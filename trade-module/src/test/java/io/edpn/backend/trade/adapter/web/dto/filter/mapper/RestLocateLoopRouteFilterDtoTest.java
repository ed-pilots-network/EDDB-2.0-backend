package io.edpn.backend.trade.adapter.web.dto.filter.mapper;

import io.edpn.backend.trade.adapter.web.dto.filter.RestLocateLoopRouteFilterDto;
import io.edpn.backend.trade.application.domain.LandingPadSize;
import io.edpn.backend.trade.application.domain.filter.LocateLoopTradeFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class RestLocateLoopRouteFilterDtoTest {
    
    private RestLocateLoopRouteFilterDtoMapper underTest;
    
    @BeforeEach
    public void setUp() {
        underTest = new RestLocateLoopRouteFilterDtoMapper();
    }
    
    @Test
    public void testMap_givenDto_shouldReturnDomainObject() {
        RestLocateLoopRouteFilterDto dto = new RestLocateLoopRouteFilterDto(
                1.0,
                2.0,
                3.0,
                List.of("Display", "Names"),
                72,
                80,
                "MEDIUM",
                5000,
                10000,
                10000,
                true,
                false);
        
        LocateLoopTradeFilter domainObject = underTest.map(dto);
        
        assertThat(domainObject.getXCoordinate(), is(1.0));
        assertThat(domainObject.getYCoordinate(), is(2.0));
        assertThat(domainObject.getZCoordinate(), is(3.0));
        assertThat(domainObject.getCommodityDisplayNames(), contains("Display", "Names"));
        assertThat(domainObject.getMaxPriceAgeHours(), is(72));
        assertThat(domainObject.getMaxRouteDistance(), is(80));
        assertThat(domainObject.getMaxLandingPadSize(), is(LandingPadSize.MEDIUM));
        assertThat(domainObject.getMaxArrivalDistance(), is(5000));
        assertThat(domainObject.getMinSupply(), is(10000));
        assertThat(domainObject.getMinDemand(), is(10000));
        assertThat(domainObject.getIncludeSurfaceStations(), is(true));
        assertThat(domainObject.getIncludeFleetCarriers(), is(false));
    }
 
}
