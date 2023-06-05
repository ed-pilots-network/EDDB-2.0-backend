package io.edpn.backend.commodityfinder.configuration;

import io.edpn.backend.commodityfinder.application.controller.BestCommodityPriceController;
import io.edpn.backend.commodityfinder.application.mappers.BestCommodityPriceResponseMapper;
import io.edpn.backend.commodityfinder.domain.usecase.FindBestCommodityPriceUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestMapperConfig {

    @Bean
    public BestCommodityPriceResponseMapper bestCommodityPriceResponseMapper() {
        return new BestCommodityPriceResponseMapper();
    }
    @Bean
    public BestCommodityPriceController bestCommodityPriceController(FindBestCommodityPriceUseCase findBestCommodityPriceUseCase,BestCommodityPriceResponseMapper bestCommodityPriceResponseMapper){
        return new BestCommodityPriceController(findBestCommodityPriceUseCase,bestCommodityPriceResponseMapper);
    }
}
