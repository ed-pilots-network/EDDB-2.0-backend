package io.edpn.backend.trade.adapter.persistence.entity.mapper;

import io.edpn.backend.trade.adapter.persistence.entity.MybatisSingleHopEntity;
import io.edpn.backend.trade.application.domain.SingleHopRoute;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MybatisSingleHopEntityMapper {
    
    private final MybatisValidatedCommodityEntityMapper mybatisValidatedCommodityEntityMapper;
    private final MybatisStationEntityMapper mybatisStationEntityMapper;
    
    public SingleHopRoute map(MybatisSingleHopEntity mybatisSingleHopEntity) {
        
        return new SingleHopRoute(
                mybatisValidatedCommodityEntityMapper.map(mybatisSingleHopEntity.getCommodityEntity()),
                mybatisStationEntityMapper.map(mybatisSingleHopEntity.getBuyFromStationEntity()),
                mybatisSingleHopEntity.getBuyPrice(),
                mybatisSingleHopEntity.getStock(),
                mybatisStationEntityMapper.map(mybatisSingleHopEntity.getSellToStationEntity()),
                mybatisSingleHopEntity.getSellPrice(),
                mybatisSingleHopEntity.getDemand(),
                mybatisSingleHopEntity.getProfit(),
                mybatisSingleHopEntity.getRouteDistance(),
                mybatisSingleHopEntity.getDistanceFromReferenceStation()
        );
    }
}
