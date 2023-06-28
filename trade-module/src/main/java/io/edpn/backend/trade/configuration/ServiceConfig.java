package io.edpn.backend.trade.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.backend.trade.application.mappers.v1.CommodityMarketInfoResponseMapper;
import io.edpn.backend.trade.application.service.v1.DefaultBestCommodityPriceService;
import io.edpn.backend.trade.application.service.RequestStationArrivalDistanceService;
import io.edpn.backend.trade.application.service.RequestStationLandingPadSizeService;
import io.edpn.backend.trade.application.service.RequestSystemCoordinatesService;
import io.edpn.backend.trade.application.service.RequestSystemEliteIdService;
import io.edpn.backend.trade.domain.model.Station;
import io.edpn.backend.trade.domain.model.System;
import io.edpn.backend.trade.domain.repository.RequestDataMessageRepository;
import io.edpn.backend.trade.domain.service.v1.BestCommodityPriceService;
import io.edpn.backend.trade.domain.service.RequestDataService;
import io.edpn.backend.trade.domain.usecase.FindCommodityMarketInfoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("TradeModuleServiceConfig")
public class ServiceConfig {

    @Bean(name = "TradeModuleBestCommodityPriceService")
    public BestCommodityPriceService bestCommodityPriceController(FindCommodityMarketInfoUseCase findCommodityMarketInfoUseCase, CommodityMarketInfoResponseMapper commodityMarketInfoResponseMapper) {
        return new DefaultBestCommodityPriceService(findCommodityMarketInfoUseCase, commodityMarketInfoResponseMapper);
    }

    @Bean(name = "TradeModuleRequestStationArrivalDistanceService")
    public RequestDataService<Station> requestStationArrivalDistanceService(RequestDataMessageRepository requestDataMessageRepository, ObjectMapper objectMapper) {
        return new RequestStationArrivalDistanceService(requestDataMessageRepository, objectMapper);
    }

    @Bean(name = "TradeModuleRequestStationLandingPadSizeService")
    public RequestDataService<Station> requestStationLandingPadSizeService(RequestDataMessageRepository requestDataMessageRepository, ObjectMapper objectMapper) {
        return new RequestStationLandingPadSizeService(requestDataMessageRepository, objectMapper);
    }

    @Bean(name = "TradeModuleRequestSystemCoordinatesService")
    public RequestDataService<System> requestSystemCoordinatesService(RequestDataMessageRepository requestDataMessageRepository, ObjectMapper objectMapper) {
        return new RequestSystemCoordinatesService(requestDataMessageRepository, objectMapper);
    }

    @Bean(name = "TradeModuleRequestSystemEliteIdService")
    public RequestDataService<System> requestSystemEliteIdService(RequestDataMessageRepository requestDataMessageRepository, ObjectMapper objectMapper) {
        return new RequestSystemEliteIdService(requestDataMessageRepository, objectMapper);
    }
}
