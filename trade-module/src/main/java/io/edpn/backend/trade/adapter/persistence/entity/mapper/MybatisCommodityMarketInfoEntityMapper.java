package io.edpn.backend.trade.adapter.persistence.entity.mapper;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisCommodityMarketInfoEntity;
import io.edpn.backend.trade.application.domain.CommodityMarketInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MybatisCommodityMarketInfoEntityMapper {

    private final MybatisValidatedCommodityEntityMapper mybatisValidatedCommodityEntityMapper;
    private final MybatisStationEntityMapper mybatisStationEntityMapper;

    public CommodityMarketInfo map(MybatisCommodityMarketInfoEntity mybatisCommodityMarketInfoEntity) {
        return new CommodityMarketInfo(
                mybatisValidatedCommodityEntityMapper.map(mybatisCommodityMarketInfoEntity.getValidatedCommodity()),
                mybatisCommodityMarketInfoEntity.getMaxBuyPrice(),
                mybatisCommodityMarketInfoEntity.getMinBuyPrice(),
                mybatisCommodityMarketInfoEntity.getAvgBuyPrice(),
                mybatisCommodityMarketInfoEntity.getMaxSellPrice(),
                mybatisCommodityMarketInfoEntity.getMinSellPrice(),
                mybatisCommodityMarketInfoEntity.getAvgSellPrice(),
                mybatisCommodityMarketInfoEntity.getMeanPrice(),
                mybatisCommodityMarketInfoEntity.getTotalStock(),
                mybatisCommodityMarketInfoEntity.getTotalDemand(),
                mybatisCommodityMarketInfoEntity.getTotalStations(),
                mybatisCommodityMarketInfoEntity.getStationsWithBuyPrice(),
                mybatisCommodityMarketInfoEntity.getStationsWithSellPrice(),
                mybatisCommodityMarketInfoEntity.getStationsWithBuyPriceLowerThanAverage(),
                mybatisCommodityMarketInfoEntity.getStationsWithSellPriceHigherThanAverage(),
                mybatisStationEntityMapper.map(mybatisCommodityMarketInfoEntity.getHighestSellingToStation()),
                mybatisStationEntityMapper.map(mybatisCommodityMarketInfoEntity.getLowestBuyingFromStation())
        );
    }

    public MybatisCommodityMarketInfoEntity map(CommodityMarketInfo commodityMarketInfo) {
        return MybatisCommodityMarketInfoEntity.builder()
                .validatedCommodity(mybatisValidatedCommodityEntityMapper.map(commodityMarketInfo.validatedCommodity()))
                .maxBuyPrice(commodityMarketInfo.maxBuyPrice())
                .minBuyPrice(commodityMarketInfo.minBuyPrice())
                .avgBuyPrice(commodityMarketInfo.avgBuyPrice())
                .maxSellPrice(commodityMarketInfo.maxSellPrice())
                .minSellPrice(commodityMarketInfo.minSellPrice())
                .avgSellPrice(commodityMarketInfo.avgSellPrice())
                .meanPrice(commodityMarketInfo.meanPrice())
                .totalStock(commodityMarketInfo.totalStock())
                .totalDemand(commodityMarketInfo.totalDemand())
                .totalStations(commodityMarketInfo.totalStations())
                .stationsWithBuyPrice(commodityMarketInfo.stationsWithBuyPrice())
                .stationsWithSellPrice(commodityMarketInfo.stationsWithSellPrice())
                .stationsWithBuyPriceLowerThanAverage(commodityMarketInfo.stationsWithBuyPriceLowerThanAverage())
                .stationsWithSellPriceHigherThanAverage(commodityMarketInfo.stationsWithSellPriceHigherThanAverage())
                .highestSellingToStation(mybatisStationEntityMapper.map(commodityMarketInfo.highestSellingToStation()))
                .lowestBuyingFromStation(mybatisStationEntityMapper.map(commodityMarketInfo.lowestBuyingFromStation()))
                .build();
    }
}
