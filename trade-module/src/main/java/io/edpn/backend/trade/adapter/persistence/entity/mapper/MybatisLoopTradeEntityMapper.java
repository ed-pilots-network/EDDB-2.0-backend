package io.edpn.backend.trade.adapter.persistence.entity.mapper;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisLoopTradeEntity;
import io.edpn.backend.trade.application.domain.LoopRoute;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MybatisLoopTradeEntityMapper {
    private final MybatisSingleHopEntityMapper singleHopEntityMapper;
    
    public LoopRoute map(MybatisLoopTradeEntity mybatisLoopTrade) {
        return new LoopRoute(
                singleHopEntityMapper.map(mybatisLoopTrade.getFirstTripEntity()),
                singleHopEntityMapper.map(mybatisLoopTrade.getReturnTripEntity()),
                mybatisLoopTrade.getDistanceFromCommander());
    }
}
