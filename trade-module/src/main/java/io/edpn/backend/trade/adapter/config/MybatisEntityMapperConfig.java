package io.edpn.backend.trade.adapter.config;

import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisCommodityEntityMapper;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisCommodityMarketInfoEntityMapper;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisLocateCommodityEntityMapper;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisLoopTradeEntityMapper;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisMarketDatumEntityMapper;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisSingleHopEntityMapper;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisStationDataRequestEntityMapper;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisStationEntityMapper;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisSystemDataRequestEntityMapper;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisSystemEntityMapper;
import io.edpn.backend.trade.adapter.persistence.entity.mapper.MybatisValidatedCommodityEntityMapper;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisFindCommodityFilterMapper;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisFindStationFilterMapper;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisFindSystemFilterMapper;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisLocateCommodityFilterMapper;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisLocateLoopTradeFilterMapper;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisLocateSingleHopTradeFilterMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("TradeModuleEntityMapperConfig")
public class MybatisEntityMapperConfig {

    @Bean(name = "tradeValidatedCommodityEntityMapper")
    public MybatisValidatedCommodityEntityMapper validatedCommodityEntityMapper() {
        return new MybatisValidatedCommodityEntityMapper();
    }

    @Bean(name = "tradeFindCommodityEntityMapper")
    public MybatisFindCommodityFilterMapper findCommodityFilterMapper() {
        return new MybatisFindCommodityFilterMapper();
    }

    @Bean(name = "tradeLocateCommodityEntityMapper")
    public MybatisLocateCommodityFilterMapper locateCommodityFilterMapper() {
        return new MybatisLocateCommodityFilterMapper();
    }

    @Bean(name = "tradePersistenceFindSystemFilterMapper")
    public MybatisFindSystemFilterMapper persistenceFindSystemFilterMapper() {
        return new MybatisFindSystemFilterMapper();
    }

    @Bean(name = "tradeSystemEntityMapper")
    public MybatisSystemEntityMapper systemEntityMapper() {
        return new MybatisSystemEntityMapper();
    }

    @Bean(name = "tradeCommodityEntityMapper")
    public MybatisCommodityEntityMapper commodityEntityMapper() {
        return new MybatisCommodityEntityMapper();
    }

    @Bean(name = "tradeMarketDatumEntityMapper")
    public MybatisMarketDatumEntityMapper marketDatumEntityMapper(
            MybatisCommodityEntityMapper mybatisCommodityEntityMapper) {
        return new MybatisMarketDatumEntityMapper(mybatisCommodityEntityMapper);
    }

    @Bean(name = "tradeStationEntityMapper")
    public MybatisStationEntityMapper stationEntityMapper(
            MybatisSystemEntityMapper mybatisSystemEntityMapper,
            MybatisMarketDatumEntityMapper mybatisMarketDatumEntityMapper) {
        return new MybatisStationEntityMapper(mybatisSystemEntityMapper, mybatisMarketDatumEntityMapper);
    }

    @Bean(name = "tradeLocateCommodityFilterMapper")
    public MybatisLocateCommodityEntityMapper locateCommodityEntityMapper(
            MybatisValidatedCommodityEntityMapper mybatisValidatedCommodityEntityMapper,
            MybatisStationEntityMapper mybatisStationEntityMapper) {
        return new MybatisLocateCommodityEntityMapper(mybatisValidatedCommodityEntityMapper, mybatisStationEntityMapper);
    }
    
    @Bean(name = "tradeLocateSingleHopTradeFilterMapper")
    public MybatisLocateSingleHopTradeFilterMapper locateSingleHopTradeFilterMapper() {
        return new MybatisLocateSingleHopTradeFilterMapper();
    }
    
    @Bean(name = "tradeLocateLoopTradeFilterMapper")
    public MybatisLocateLoopTradeFilterMapper locateLoopTradeFilterMapper() {
        return new MybatisLocateLoopTradeFilterMapper();
    }
    
    @Bean(name = "tradeSingleHopTradeEntityMapper")
    public MybatisSingleHopEntityMapper singleHopEntityMapper(
            MybatisValidatedCommodityEntityMapper mybatisValidatedCommodityEntityMapper,
            MybatisStationEntityMapper mybatisStationEntityMapper){
        return new MybatisSingleHopEntityMapper(mybatisValidatedCommodityEntityMapper, mybatisStationEntityMapper);
    }
    
    @Bean(name = "tradeLoopTradeEntityMapper")
    public MybatisLoopTradeEntityMapper loopTradeEntityMapper(
            MybatisSingleHopEntityMapper mybatisSingleHopEntityMapper) {
        return new MybatisLoopTradeEntityMapper(mybatisSingleHopEntityMapper);
    }

    @Bean(name = "tradeCommodityMarketInfoEntityMapper")
    public MybatisCommodityMarketInfoEntityMapper commodityMarketInfoEntityMapper(
            MybatisValidatedCommodityEntityMapper mybatisValidatedCommodityEntityMapper,
            MybatisStationEntityMapper mybatisStationEntityMapper) {
        return new MybatisCommodityMarketInfoEntityMapper(mybatisValidatedCommodityEntityMapper, mybatisStationEntityMapper);
    }

    @Bean(name = "tradePersistenceFindStationFilterMapper")
    public MybatisFindStationFilterMapper persistenceFindStationFilterMapper() {
        return new MybatisFindStationFilterMapper();
    }

    @Bean(name = "tradeStationDataRequestEntityMapper")
    public MybatisStationDataRequestEntityMapper stationDataRequestEntityMapper() {
        return new MybatisStationDataRequestEntityMapper();
    }

    @Bean(name = "tradeSystemDataRequestEntityMapper")
    public MybatisSystemDataRequestEntityMapper systemDataRequestEntityMapper() {
        return new MybatisSystemDataRequestEntityMapper();
    }
}
