package io.edpn.backend.trade.adapter.config;

import io.edpn.backend.trade.adapter.web.dto.filter.mapper.RestFindCommodityFilterDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.filter.mapper.RestLocateCommodityFilterDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.filter.mapper.RestLocateSingleHopRouteFilterDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestCommodityMarketInfoDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestLocateCommodityDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestSingleHopRouteDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestStationDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestSystemDtoMapper;
import io.edpn.backend.trade.adapter.web.dto.object.mapper.RestValidatedCommodityDtoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("TradeWebDtoMapperConfig")
public class WebDtoMapperConfig {

    @Bean(name = "tradeFindCommodityDTOMapper")
    public RestFindCommodityFilterDtoMapper findCommodityDTOMapper() {
        return new RestFindCommodityFilterDtoMapper();
    }

    @Bean(name = "tradeValidatedCommodityDTOMapper")
    public RestValidatedCommodityDtoMapper validatedCommodityDTOMapper() {
        return new RestValidatedCommodityDtoMapper();
    }

    @Bean(name = "tradeSystemDtoMapper")
    public RestSystemDtoMapper systemDtoMapper() {
        return new RestSystemDtoMapper();
    }

    @Bean(name = "tradeStationDtoMapper")
    public RestStationDtoMapper stationDtoMapper(
            RestSystemDtoMapper restSystemDtoMapper) {
        return new RestStationDtoMapper(restSystemDtoMapper);
    }
    
    @Bean(name = "tradeSingleHopRouteMapper")
    public RestSingleHopRouteDtoMapper singleHopRouteDtoMapper(
            RestValidatedCommodityDtoMapper restValidatedCommodityDtoMapper,
            RestStationDtoMapper restStationDtoMapper
    ) {
        return new RestSingleHopRouteDtoMapper(restValidatedCommodityDtoMapper, restStationDtoMapper);
    }

    @Bean(name = "tradeLocateCommodityDtoMapper")
    public RestLocateCommodityDtoMapper locateCommodityDtoMapper(
            RestStationDtoMapper restStationDtoMapper,
            RestValidatedCommodityDtoMapper restValidatedCommodityDtoMapper) {
        return new RestLocateCommodityDtoMapper(restStationDtoMapper, restValidatedCommodityDtoMapper);
    }

    @Bean(name = "tradeCommodityMarketInfoDtoMapper")
    public RestCommodityMarketInfoDtoMapper commodityMarketInfoDtoMapper(
            RestValidatedCommodityDtoMapper restValidatedCommodityDtoMapper,
            RestStationDtoMapper restStationDtoMapper) {
        return new RestCommodityMarketInfoDtoMapper(restValidatedCommodityDtoMapper, restStationDtoMapper);
    }

    @Bean(name = "tradeLocateCommodityFilterDtoMapper")
    public RestLocateCommodityFilterDtoMapper locateCommodityFilterDtoMapper() {
        return new RestLocateCommodityFilterDtoMapper();
    }
    
    @Bean(name = "tradeLocateSingleHopRouteFilterMapper")
    public RestLocateSingleHopRouteFilterDtoMapper locateSingleHopRouteFilterDtoMapper(){
        return new RestLocateSingleHopRouteFilterDtoMapper();
    }
}
