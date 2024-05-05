package io.edpn.backend.trade.adapter.web;

import io.edpn.backend.trade.adapter.web.dto.filter.RestFindCommodityFilterDto;
import io.edpn.backend.trade.adapter.web.dto.filter.RestLocateCommodityFilterDto;
import io.edpn.backend.trade.adapter.web.dto.filter.RestLocateLoopRouteFilterDto;
import io.edpn.backend.trade.adapter.web.dto.filter.RestLocateSingleHopRouteFilterDto;
import io.edpn.backend.trade.adapter.web.dto.filter.mapper.RestFindCommodityFilterDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.filter.mapper.RestLocateCommodityFilterDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.filter.mapper.RestLocateLoopRouteFilterDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.filter.mapper.RestLocateSingleHopRouteFilterDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.RestCommodityMarketInfoDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestLocateCommodityDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestLoopRouteDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestSingleHopRouteDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestValidatedCommodityDto;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestCommodityMarketInfoDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestLocateCommodityDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestLoopRouteDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestSingleHopRouteDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestValidatedCommodityDtoMapper;
import io.edpn.backend.trade.application.domain.filter.FindCommodityFilter;
import io.edpn.backend.trade.application.domain.filter.LocateCommodityFilter;
import io.edpn.backend.trade.application.domain.filter.LocateLoopTradeFilter;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.port.incomming.commoditymarketinfo.GetFullCommodityMarketInfoUseCase;
import io.edpn.backend.trade.application.port.incomming.locatecommodity.LocateCommodityUseCase;
import io.edpn.backend.trade.application.port.incomming.locatetraderoute.LocateLoopTradeRouteUseCase;
import io.edpn.backend.trade.application.port.incomming.locatetraderoute.LocateSingleHopTradeRouteUseCase;
import io.edpn.backend.trade.application.port.incomming.validatedcommodity.FindAllValidatedCommodityUseCase;
import io.edpn.backend.trade.application.port.incomming.validatedcommodity.FindValidatedCommodityByFilterUseCase;
import io.edpn.backend.trade.application.port.incomming.validatedcommodity.FindValidatedCommodityByNameUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/v1/trade")
public class TradeModuleController {

    private final FindAllValidatedCommodityUseCase findAllValidatedCommodityUseCase;
    private final FindValidatedCommodityByNameUseCase findValidatedCommodityByNameUseCase;
    private final FindValidatedCommodityByFilterUseCase findValidatedCommodityByFilterUseCase;
    private final LocateCommodityUseCase locateCommodityUseCase;
    private final GetFullCommodityMarketInfoUseCase getFullCommodityMarketInfoUseCase;
    private final LocateSingleHopTradeRouteUseCase locateSingleHopTradeRouteUseCase;
    private final LocateLoopTradeRouteUseCase locateLoopTradeRouteUseCase;
    
    private final RestValidatedCommodityDtoMapper restValidatedCommodityDtoMapper;
    private final RestLocateCommodityDtoMapper restLocateCommodityDtoMapper;
    private final RestCommodityMarketInfoDtoMapper restCommodityMarketInfoDtoMapper;
    private final RestSingleHopRouteDtoMapper restSingleHopRouteDtoMapper;
    private final RestLoopRouteDtoMapper restLoopRouteDtoMapper;

    private final RestFindCommodityFilterDtoMapper restFindCommodityFilterDtoMapper;
    private final RestLocateCommodityFilterDtoMapper restLocateCommodityFilterDtoMapper;
    private final RestLocateSingleHopRouteFilterDtoMapper restLocateSingleHopRouteFilterDtoMapper;
    private final RestLocateLoopRouteFilterDtoMapper restLocateLoopRouteFilterDtoMapper;

    @GetMapping("/commodity")
    public List<RestValidatedCommodityDto> findAll() {
        return findAllValidatedCommodityUseCase.findAll()
                .stream()
                .map(restValidatedCommodityDtoMapper::map)
                .toList();
    }

    @GetMapping("/commodity/filter")
    public List<RestValidatedCommodityDto> findByFilter(RestFindCommodityFilterDto findCommodityRequest) {
        FindCommodityFilter findCommodityFilter = restFindCommodityFilterDtoMapper.map(findCommodityRequest);

        return findValidatedCommodityByFilterUseCase
                .findByFilter(findCommodityFilter)
                .stream()
                .map(restValidatedCommodityDtoMapper::map)
                .toList();
    }

    @GetMapping("/commodity/{displayName}")
    public Optional<RestValidatedCommodityDto> findByName(@PathVariable String displayName) {
        return findValidatedCommodityByNameUseCase
                .findByName(displayName)
                .map(restValidatedCommodityDtoMapper::map);
    }

    @GetMapping("/locate-commodity/filter")
    public List<RestLocateCommodityDto> locateCommodityWithFilters(RestLocateCommodityFilterDto restLocateCommodityFilterDto) {
        LocateCommodityFilter locateCommodityFilter = restLocateCommodityFilterDtoMapper.map(restLocateCommodityFilterDto);

        return locateCommodityUseCase
                .locateCommodityOrderByDistance(locateCommodityFilter)
                .stream()
                .map(restLocateCommodityDtoMapper::map)
                .toList();
    }

    @GetMapping("/best-price")
    List<RestCommodityMarketInfoDto> fullMarketInfo() {
        return getFullCommodityMarketInfoUseCase
                .findAll()
                .stream()
                .map(restCommodityMarketInfoDtoMapper::map)
                .toList();
    }
    
    @Operation(summary = "Find one directional trade with filter. At least one system must be specified, station cannot be specified without system, cargo evaluates to a minSupply = capacity and a minDemand = 720 * 4 (or 0) for lossless trades.")
    @GetMapping("/locate-trade/single")
    public List<RestSingleHopRouteDto> locateSingleHopTradeWithFilter(RestLocateSingleHopRouteFilterDto locateSingleHopeRouteFilterDto) {
        LocateSingleHopTradeFilter locateSingleHopTradeFilter = restLocateSingleHopRouteFilterDtoMapper.map(locateSingleHopeRouteFilterDto);
        
        return locateSingleHopTradeRouteUseCase
                .locateRoutesOrderByProfit(locateSingleHopTradeFilter)
                .stream()
                .map(restSingleHopRouteDtoMapper::map)
                .toList();
    }
    
    @GetMapping("/locate-trade/loop")
    public List<RestLoopRouteDto> locateLoopTradeWithFilter(RestLocateLoopRouteFilterDto locateLoopRouteFilterDto){
        LocateLoopTradeFilter locateLoopTradeFilter = restLocateLoopRouteFilterDtoMapper.map(locateLoopRouteFilterDto);
        
        return locateLoopTradeRouteUseCase
                .locateRoutesOrderByProfit(locateLoopTradeFilter)
                .stream()
                .map(restLoopRouteDtoMapper::map)
                .toList();
        
    }
}
