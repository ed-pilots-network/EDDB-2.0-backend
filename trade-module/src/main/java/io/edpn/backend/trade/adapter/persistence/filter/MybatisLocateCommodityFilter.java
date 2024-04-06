package io.edpn.backend.trade.adapter.persistence.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class MybatisLocateCommodityFilter {

    private String commodityDisplayName;
    private Double xCoordinate;
    private Double yCoordinate;
    private Double zCoordinate;
    private Boolean includePlanetary;
    private Boolean includeOdyssey;
    private Boolean includeFleetCarriers;
    private Integer maxLandingPadSize;
    private Long minSupply;
    private Long minDemand;
}
