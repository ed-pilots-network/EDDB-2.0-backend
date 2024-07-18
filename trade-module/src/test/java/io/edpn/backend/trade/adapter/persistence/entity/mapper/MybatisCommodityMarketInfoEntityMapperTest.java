package io.edpn.backend.trade.adapter.persistence.entity.mapper;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisCommodityMarketInfoEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisStationEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisValidatedCommodityEntity;
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
class MybatisCommodityMarketInfoEntityMapperTest {

    @Mock
    private MybatisValidatedCommodityEntityMapper mybatisValidatedCommodityEntityMapper;

    @Mock
    private MybatisStationEntityMapper mybatisStationEntityMapper;

    private MybatisCommodityMarketInfoEntityMapper underTest;

    @BeforeEach
    public void setUp() {
        underTest = new MybatisCommodityMarketInfoEntityMapper(mybatisValidatedCommodityEntityMapper, mybatisStationEntityMapper);
    }

    @Test
    public void testMap_givenEntity_shouldReturnDomainObject() {

        ValidatedCommodity mockCommodity = mock(ValidatedCommodity.class);
        Station mockHighestSellingStation = mock(Station.class);
        Station mockLowestBuyingStation = mock(Station.class);

        // Setup the CommodityMarketInfoEntity with test data
        MybatisCommodityMarketInfoEntity entity = MybatisCommodityMarketInfoEntity.builder()
                .validatedCommodity(mock(MybatisValidatedCommodityEntity.class))
                .maxBuyPrice(100.0)
                .minBuyPrice(50.0)
                .avgBuyPrice(75.0)
                .maxSellPrice(200.0)
                .minSellPrice(100.0)
                .avgSellPrice(150.0)
                .meanPrice(120.0)
                .totalStock(1000L)
                .totalDemand(500L)
                .totalStations(10)
                .stationsWithBuyPrice(5)
                .stationsWithSellPrice(4)
                .stationsWithBuyPriceLowerThanAverage(2)
                .stationsWithSellPriceHigherThanAverage(1)
                .highestSellingToStation(mock(MybatisStationEntity.class))
                .lowestBuyingFromStation(mock(MybatisStationEntity.class))
                .build();

        when(mybatisValidatedCommodityEntityMapper.map(entity.getValidatedCommodity())).thenReturn(mockCommodity);
        when(mybatisStationEntityMapper.map(entity.getHighestSellingToStation())).thenReturn(mockHighestSellingStation);
        when(mybatisStationEntityMapper.map(entity.getLowestBuyingFromStation())).thenReturn(mockLowestBuyingStation);

        // Map the entity to a CommodityMarketInfo object
        CommodityMarketInfo result = underTest.map(entity);

        // Verify that the result matches the expected values
        assertThat(result.validatedCommodity(), is(mockCommodity));
        assertThat(result.maxBuyPrice(), is(100.0));
        assertThat(result.minBuyPrice(), is(50.0));
        assertThat(result.avgBuyPrice(), is(75.0));
        assertThat(result.maxSellPrice(), is(200.0));
        assertThat(result.minSellPrice(), is(100.0));
        assertThat(result.avgSellPrice(), is(150.0));
        assertThat(result.meanPrice(), is(120.0));
        assertThat(result.totalStock(), is(1000L));
        assertThat(result.totalDemand(), is(500L));
        assertThat(result.totalStations(), is(10));
        assertThat(result.stationsWithBuyPrice(), is(5));
        assertThat(result.stationsWithSellPrice(), is(4));
        assertThat(result.stationsWithBuyPriceLowerThanAverage(), is(2));
        assertThat(result.stationsWithSellPriceHigherThanAverage(), is(1));
        assertThat(result.highestSellingToStation(), is(mockHighestSellingStation));
        assertThat(result.lowestBuyingFromStation(), is(mockLowestBuyingStation));

        verify(mybatisValidatedCommodityEntityMapper, times(1)).map(entity.getValidatedCommodity());
        verify(mybatisStationEntityMapper, times(1)).map(entity.getHighestSellingToStation());
        verify(mybatisStationEntityMapper, times(1)).map(entity.getLowestBuyingFromStation());
    }

    @Test
    public void testMap_givenDomainObject_shouldReturnEntity() {

        MybatisValidatedCommodityEntity mockCommodityEntity = mock(MybatisValidatedCommodityEntity.class);
        MybatisStationEntity mockHighestSellingMybatisStationEntity = mock(MybatisStationEntity.class);
        MybatisStationEntity mockLowestBuyingMybatisStationEntity = mock(MybatisStationEntity.class);

        // Setup the CommodityMarketInfo with test data
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

        when(mybatisValidatedCommodityEntityMapper.map(domainObject.validatedCommodity())).thenReturn(mockCommodityEntity);
        when(mybatisStationEntityMapper.map(domainObject.highestSellingToStation())).thenReturn(mockHighestSellingMybatisStationEntity);
        when(mybatisStationEntityMapper.map(domainObject.lowestBuyingFromStation())).thenReturn(mockLowestBuyingMybatisStationEntity);

        // Map the domainObject to a CommodityMarketInfoEntity
        MybatisCommodityMarketInfoEntity result = underTest.map(domainObject);

        // Verify that the result matches the expected values
        assertThat(result.getValidatedCommodity(), is(mockCommodityEntity));
        assertThat(result.getMaxBuyPrice(), is(100.0));
        assertThat(result.getMinBuyPrice(), is(50.0));
        assertThat(result.getAvgBuyPrice(), is(75.0));
        assertThat(result.getMaxSellPrice(), is(200.0));
        assertThat(result.getMinSellPrice(), is(100.0));
        assertThat(result.getAvgSellPrice(), is(150.0));
        assertThat(result.getMeanPrice(), is(120.0));
        assertThat(result.getTotalStock(), is(1000L));
        assertThat(result.getTotalDemand(), is(500L));
        assertThat(result.getTotalStations(), is(10));
        assertThat(result.getStationsWithBuyPrice(), is(5));
        assertThat(result.getStationsWithSellPrice(), is(4));
        assertThat(result.getStationsWithBuyPriceLowerThanAverage(), is(2));
        assertThat(result.getStationsWithSellPriceHigherThanAverage(), is(1));
        assertThat(result.getHighestSellingToStation(), is(mockHighestSellingMybatisStationEntity));
        assertThat(result.getLowestBuyingFromStation(), is(mockLowestBuyingMybatisStationEntity));

        verify(mybatisValidatedCommodityEntityMapper, times(1)).map(domainObject.validatedCommodity());
        verify(mybatisStationEntityMapper, times(1)).map(domainObject.highestSellingToStation());
        verify(mybatisStationEntityMapper, times(1)).map(domainObject.lowestBuyingFromStation());
    }

}