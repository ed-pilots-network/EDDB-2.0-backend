package io.edpn.backend.trade.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class CommodityMarketInfo {

    private ValidatedCommodity validatedCommodity;
    private Double maxBuyPrice;
    private Double minBuyPrice;
    private Double avgBuyPrice;
    private Double maxSellPrice;
    private Double minSellPrice;
    private Double avgSellPrice;
    private Double minMeanPrice;
    private Double maxMeanPrice;
    private Double averageMeanPrice;
    private Long totalStock;
    private Long totalDemand;
    private Integer totalStations;
    private Integer stationsWithBuyPrice;
    private Integer stationsWithSellPrice;
    private Integer stationsWithBuyPriceLowerThanAverage;
    private Integer stationsWithSellPriceHigherThanAverage;
    private Station highestSellingToStation;
    private Station lowestBuyingFromStation;
}

