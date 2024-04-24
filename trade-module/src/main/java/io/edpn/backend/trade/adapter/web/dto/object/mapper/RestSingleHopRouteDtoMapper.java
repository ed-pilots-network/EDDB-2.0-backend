package io.edpn.backend.trade.adapter.web.dto.object.mapper;

import io.edpn.backend.trade.adapter.web.dto.object.RestSingleHopRouteDto;
import io.edpn.backend.trade.application.domain.SingleHopRoute;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestSingleHopRouteDtoMapper {
    
    private final RestValidatedCommodityDtoMapper validatedCommodityDtoMapper;
    private final RestStationDtoMapper stationDtoMapper;
    
    public RestSingleHopRouteDto map(SingleHopRoute singleHopRoute) {
        
        return RestSingleHopRouteDto.builder()
                .commodityDto(validatedCommodityDtoMapper.map(singleHopRoute.commodity()))
                .buyFromStationDto(stationDtoMapper.map(singleHopRoute.buyFromStation()))
                .buyPrice(singleHopRoute.buyPrice())
                .stock(singleHopRoute.stock())
                .sellToStationDto(stationDtoMapper.map(singleHopRoute.sellToStation()))
                .sellPrice(singleHopRoute.sellPrice())
                .demand(singleHopRoute.demand())
                .profit(singleHopRoute.profit())
                .routeDistance(singleHopRoute.routeDistance())
                .build();
    }
}
