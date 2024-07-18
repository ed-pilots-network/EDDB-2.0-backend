package io.edpn.backend.trade.adapter.web.dto.object.mapper;

import io.edpn.backend.trade.adapter.web.dto.object.RestCommodityMarketInfoDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestStationDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestValidatedCommodityDto;
import io.edpn.backend.trade.application.domain.CommodityMarketInfo;
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
class RestCommodityMarketInfoDtoMapperTest {

    @Mock
    private RestValidatedCommodityDtoMapper commodityDtoMapper;

    @Mock
    private RestStationDtoMapper restStationDtoMapper;

    private RestCommodityMarketInfoDtoMapper underTest;

    @BeforeEach
    public void setUp() {
        underTest = new RestCommodityMarketInfoDtoMapper(commodityDtoMapper, restStationDtoMapper);
    }

    @Test
    public void testMap_givenDomainObject_shouldReturnDto() {

        RestValidatedCommodityDto mockCommodityDto = mock(RestValidatedCommodityDto.class);
        RestStationDto mockHighestSellingRestStationDto = mock(RestStationDto.class);
        RestStationDto mockLowestBuyingRestStationDto = mock(RestStationDto.class);

        CommodityMarketInfo domainObject = new CommodityMarketInfo(
                mock(ValidatedCommodity.class),
                100.0,
                50.0,
                75.0,
                200.0,
                100.0,
                150.0,
                120.0,
                1000L,
                500L,
                10,
                5,
                4,
                2,
                1,
                mock(Station.class),
                mock(Station.class)
        );

        when(commodityDtoMapper.map(domainObject.validatedCommodity())).thenReturn(mockCommodityDto);
        when(restStationDtoMapper.map(domainObject.highestSellingToStation())).thenReturn(mockHighestSellingRestStationDto);
        when(restStationDtoMapper.map(domainObject.lowestBuyingFromStation())).thenReturn(mockLowestBuyingRestStationDto);

        RestCommodityMarketInfoDto dto = underTest.map(domainObject);

        assertThat(dto.commodity(), is(mockCommodityDto));
        assertThat(dto.maxBuyPrice(), is(100.0));
        assertThat(dto.minBuyPrice(), is(50.0));
        assertThat(dto.avgBuyPrice(), is(75.0));
        assertThat(dto.maxSellPrice(), is(200.0));
        assertThat(dto.minSellPrice(), is(100.0));
        assertThat(dto.avgSellPrice(), is(150.0));
        assertThat(dto.meanPrice(), is(120.0));
        assertThat(dto.totalStock(), is(1000L));
        assertThat(dto.totalDemand(), is(500L));
        assertThat(dto.totalStations(), is(10));
        assertThat(dto.stationsWithBuyPrice(), is(5));
        assertThat(dto.stationsWithSellPrice(), is(4));
        assertThat(dto.stationsWithBuyPriceLowerThanAverage(), is(2));
        assertThat(dto.stationsWithSellPriceHigherThanAverage(), is(1));
        assertThat(dto.highestSellingToStation(), is(mockHighestSellingRestStationDto));
        assertThat(dto.lowestBuyingFromStation(), is(mockLowestBuyingRestStationDto));

        verify(commodityDtoMapper, times(1)).map(domainObject.validatedCommodity());
        verify(restStationDtoMapper, times(1)).map(domainObject.highestSellingToStation());
        verify(restStationDtoMapper, times(1)).map(domainObject.lowestBuyingFromStation());
    }
}