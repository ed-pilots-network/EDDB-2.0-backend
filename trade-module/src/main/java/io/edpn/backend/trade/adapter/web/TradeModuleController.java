package io.edpn.backend.trade.adapter.web;

import io.edpn.backend.trade.adapter.web.dto.filter.RestFindCommodityFilterDto;
import io.edpn.backend.trade.adapter.web.dto.filter.RestLocateCommodityFilterDto;
import io.edpn.backend.trade.adapter.web.dto.filter.RestLocateSingleHopRouteFilterDto;
import io.edpn.backend.trade.adapter.web.dto.filter.mapper.RestFindCommodityFilterDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.filter.mapper.RestLocateCommodityFilterDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.filter.mapper.RestLocateSingleHopRouteFilterDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.RestCommodityMarketInfoDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestLocateCommodityDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestSingleHopRouteDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestStationDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestSystemDto;
import io.edpn.backend.trade.adapter.web.dto.object.RestValidatedCommodityDto;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestCommodityMarketInfoDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestLocateCommodityDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestSingleHopRouteDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestStationDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestSystemDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestValidatedCommodityDtoMapper;
import io.edpn.backend.trade.adapter.web.validator.FindSystemsByNameContainingInputValidator;
import io.edpn.backend.trade.application.domain.filter.FindCommodityFilter;
import io.edpn.backend.trade.application.domain.filter.LocateCommodityFilter;
import io.edpn.backend.trade.application.domain.filter.LocateSingleHopTradeFilter;
import io.edpn.backend.trade.application.port.incomming.commoditymarketinfo.GetFullCommodityMarketInfoUseCase;
import io.edpn.backend.trade.application.port.incomming.locatecommodity.LocateCommodityUseCase;
import io.edpn.backend.trade.application.port.incomming.locatetraderoute.LocateSingleHopTradeRouteUseCase;
import io.edpn.backend.trade.application.port.incomming.station.FindStationsBySystemNameUseCase;
import io.edpn.backend.trade.application.port.incomming.system.FindSystemsByNameContainingUseCase;
import io.edpn.backend.trade.application.port.incomming.validatedcommodity.FindAllValidatedCommodityUseCase;
import io.edpn.backend.trade.application.port.incomming.validatedcommodity.FindValidatedCommodityByFilterUseCase;
import io.edpn.backend.trade.application.port.incomming.validatedcommodity.FindValidatedCommodityByNameUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private final FindSystemsByNameContainingUseCase findSystemsByNameContainingUseCase;
    private final FindStationsBySystemNameUseCase findStationsBySystemNameUseCase;
    
    private final FindSystemsByNameContainingInputValidator findSystemsByNameContainingInputValidator;
    
    private final RestValidatedCommodityDtoMapper restValidatedCommodityDtoMapper;
    private final RestLocateCommodityDtoMapper restLocateCommodityDtoMapper;
    private final RestCommodityMarketInfoDtoMapper restCommodityMarketInfoDtoMapper;
    private final RestSingleHopRouteDtoMapper restSingleHopRouteDtoMapper;
    private final RestSystemDtoMapper systemDtoMapper;
    private final RestStationDtoMapper stationDtoMapper;

    private final RestFindCommodityFilterDtoMapper restFindCommodityFilterDtoMapper;
    private final RestLocateCommodityFilterDtoMapper restLocateCommodityFilterDtoMapper;
    private final RestLocateSingleHopRouteFilterDtoMapper restLocateSingleHopRouteFilterDtoMapper;

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
    
    @GetMapping("/locate-trade/single")
    public List<RestSingleHopRouteDto> locateSingleHopTradeWithFilter(RestLocateSingleHopRouteFilterDto locateSingleHopeRouteFilterDto) {
        LocateSingleHopTradeFilter locateSingleHopTradeFilter = restLocateSingleHopRouteFilterDtoMapper.map(locateSingleHopeRouteFilterDto);
        
        return locateSingleHopTradeRouteUseCase
                .locateRoutesOrderByProfit(locateSingleHopTradeFilter)
                .stream()
                .map(restSingleHopRouteDtoMapper::map)
                .toList();
    }
    
    @Operation(summary = "Find systems by name containing a specific substring, will give priority to return systems starting with the given substring")
    @GetMapping("/system/by-name-containing")
    public List<RestSystemDto> byNameContaining(
            @Parameter(description = "Substring to search for in the system name, minimal 3 characters", required = true) @RequestParam(name = "subString") String subString,
            @Parameter(description = "Amount of results to retrieve. values 1...100", required = false) @RequestParam(name = "amount", required = false, defaultValue = "10") Integer amount){
        findSystemsByNameContainingInputValidator.validateAmount(amount);
        findSystemsByNameContainingInputValidator.validateSubString(subString);
        
        return findSystemsByNameContainingUseCase
                .findSystemsByNameContaining(subString, amount)
                .stream()
                .map(systemDtoMapper::map)
                .toList();
    }
    
    @GetMapping("/station/by-system-name")
    public List<RestStationDto> bySystemName(String systemName){
        
        return findStationsBySystemNameUseCase
                .findStationsBySystemName(systemName)
                .stream()
                .map(stationDtoMapper::map)
                .toList();
    }
}
