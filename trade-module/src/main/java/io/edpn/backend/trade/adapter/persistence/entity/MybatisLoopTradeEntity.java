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
public class MybatisLoopTradeEntity {
    
    private MybatisSingleHopEntity firstTripEntity;
    private MybatisSingleHopEntity returnTripEntity;
    private Double distanceFromCommander;
}
