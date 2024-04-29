package io.edpn.backend.trade.adapter.web.dto.filter.mapper;

import io.edpn.backend.trade.adapter.web.dto.filter.RestLocateSingleHopRouteFilterDto;
import io.edpn.backend.trade.application.domain.LandingPadSize;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class RestLocateSingleHopRouteFilterDtoTest {
    
    private RestLocateSingleHopRouteFilterDtoMapper underTest;
    
    @BeforeEach
    public void setUp() {
        underTest = new RestLocateSingleHopRouteFilterDtoMapper();
    }
    
    @Test
    public void testMap_givenDto_shouldReturnDomainObject() {
        RestLocateSingleHopRouteFilterDto dto = new RestLocateSingleHopRouteFilterDto(
                "BuyFromSystem",
                "BuyFromStation",
                "SellToSystem",
                "SellToStation",
                List.of("Display", "Names"),
                72,
                80,
                "MEDIUM",
                5000,
                720,
                true,
                false);
        
        LocateSingleHopTradeFilter domainObject = underTest.map(dto);
        
        assertThat(domainObject.getBuyFromSystemName(), is("BuyFromSystem"));
        assertThat(domainObject.getBuyFromStationName(), is("BuyFromStation"));
        assertThat(domainObject.getSellToSystemName(), is("SellToSystem"));
        assertThat(domainObject.getSellToStationName(), is("SellToStation"));
        assertThat(domainObject.getCommodityDisplayNames(), contains("Display", "Names"));
        assertThat(domainObject.getMaxPriceAgeHours(), is(72));
        assertThat(domainObject.getMaxRouteDistance(), is(80));
        assertThat(domainObject.getMaxLandingPadSize(), is(LandingPadSize.MEDIUM));
        assertThat(domainObject.getMaxArrivalDistance(), is(5000));
        assertThat(domainObject.getCargoCapacity(), is(720));
        assertThat(domainObject.getIncludeSurfaceStations(), is(true));
        assertThat(domainObject.getIncludeFleetCarriers(), is(false));
    }
 
}
