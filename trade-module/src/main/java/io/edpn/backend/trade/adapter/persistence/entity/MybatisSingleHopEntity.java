package io.edpn.backend.trade.adapter.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MybatisSingleHopEntity {
    
    private MybatisValidatedCommodityEntity commodityEntity;
    private MybatisStationEntity buyFromStationEntity;
    private Long buyPrice;
    private Long stock;
    private MybatisStationEntity sellToStationEntity;
    private Long sellPrice;
    private Long demand;
    private Long profit;
    private Double routeDistance;
    private Double distanceFromReferenceStation;
}
