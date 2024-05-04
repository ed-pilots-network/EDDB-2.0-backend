package io.edpn.backend.trade.adapter.web.dto.object.mapper;

import io.edpn.backend.trade.adapter.web.dto.object.RestLoopRouteDto;
import io.edpn.backend.trade.application.domain.LoopRoute;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestLoopRouteDtoMapper {
    
    private final RestSingleHopRouteDtoMapper restSingleHopRouteDtoMapper;
    
    public RestLoopRouteDto map(LoopRoute loopRoute) {
        return new RestLoopRouteDto(
                restSingleHopRouteDtoMapper.map(loopRoute.firstTrip()),
                restSingleHopRouteDtoMapper.map(loopRoute.returnTrip()),
                loopRoute.distanceFromCommander());
    }
}
