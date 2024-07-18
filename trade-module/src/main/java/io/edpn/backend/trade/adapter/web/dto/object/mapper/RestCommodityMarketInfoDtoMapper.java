package io.edpn.backend.trade.adapter.web.dto.object.mapper;

import io.edpn.backend.trade.adapter.web.dto.object.RestCommodityMarketInfoDto;
import io.edpn.backend.trade.application.domain.CommodityMarketInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestCommodityMarketInfoDtoMapper {

    private final RestValidatedCommodityDtoMapper commodityDtoMapper;
    private final RestStationDtoMapper restStationDtoMapper;

    public RestCommodityMarketInfoDto map(CommodityMarketInfo commodityMarketInfo) {
        return new RestCommodityMarketInfoDto(
                commodityDtoMapper.map(commodityMarketInfo.validatedCommodity()),
                commodityMarketInfo.maxBuyPrice(),
                commodityMarketInfo.minBuyPrice(),
                commodityMarketInfo.avgBuyPrice(),
                commodityMarketInfo.maxSellPrice(),
                commodityMarketInfo.minSellPrice(),
                commodityMarketInfo.avgSellPrice(),
                commodityMarketInfo.meanPrice(),
                commodityMarketInfo.totalStock(),
                commodityMarketInfo.totalDemand(),
                commodityMarketInfo.totalStations(),
                commodityMarketInfo.stationsWithBuyPrice(),
                commodityMarketInfo.stationsWithSellPrice(),
                commodityMarketInfo.stationsWithBuyPriceLowerThanAverage(),
                commodityMarketInfo.stationsWithSellPriceHigherThanAverage(),
                restStationDtoMapper.map(commodityMarketInfo.highestSellingToStation()),
                restStationDtoMapper.map(commodityMarketInfo.lowestBuyingFromStation())
        );
    }
}
