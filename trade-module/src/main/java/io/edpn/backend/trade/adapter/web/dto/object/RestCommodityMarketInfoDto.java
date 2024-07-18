package io.edpn.backend.trade.adapter.web.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "CommodityMarketInfoDto")
@Builder
@Jacksonized
public record RestCommodityMarketInfoDto(
        RestValidatedCommodityDto commodity,
        Double maxBuyPrice,
        Double minBuyPrice,
        Double avgBuyPrice,
        Double maxSellPrice,
        Double minSellPrice,
        Double avgSellPrice,
        Double meanPrice,
        Long totalStock,
        Long totalDemand,
        Integer totalStations,
        Integer stationsWithBuyPrice,
        Integer stationsWithSellPrice,
        Integer stationsWithBuyPriceLowerThanAverage,
        Integer stationsWithSellPriceHigherThanAverage,
        RestStationDto highestSellingToStation,
        RestStationDto lowestBuyingFromStation) {
}
