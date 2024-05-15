package io.edpn.backend.trade.adapter.config;

import io.edpn.backend.trade.adapter.persistence.CommodityMarketInfoRepository;
import io.edpn.backend.trade.adapter.persistence.CommodityRepository;
import io.edpn.backend.trade.adapter.persistence.LatestMarketDatumRepository;
import io.edpn.backend.trade.adapter.persistence.LocateCommodityRepository;
import io.edpn.backend.trade.adapter.persistence.LocateTradeRouteRepository;
import io.edpn.backend.trade.adapter.persistence.MarketDatumRepository;
import io.edpn.backend.trade.adapter.persistence.StationArrivalDistanceRequestRepository;
import io.edpn.backend.trade.adapter.persistence.StationLandingPadSizeRequestRepository;
import io.edpn.backend.trade.adapter.persistence.StationPlanetaryRequestRepository;
import io.edpn.backend.trade.adapter.persistence.StationRepository;
import io.edpn.backend.trade.adapter.persistence.StationRequireOdysseyRequestRepository;
import io.edpn.backend.trade.adapter.persistence.SystemCoordinateRequestRepository;
import io.edpn.backend.trade.adapter.persistence.SystemEliteIdRequestRepository;
import io.edpn.backend.trade.adapter.persistence.SystemRepository;
import io.edpn.backend.trade.adapter.persistence.ValidatedCommodityRepository;
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
import io.edpn.backend.trade.adapter.persistence.repository.MybatisCommodityMarketInfoRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisCommodityRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLatestMarketDatumRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateCommodityRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateLoopTradeRouteRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateSingleHopTradeRouteRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisMarketDatumRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationArrivalDistanceRequestRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationLandingPadSizeRequestRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationPlanetaryRequestRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRequireOdysseyRequestRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisSystemCoordinateRequestRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisSystemEliteIdRequestRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisSystemRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisValidatedCommodityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("TradeRepositoryConfig")
public class RepositoryConfig {
    
    @Bean(name = "tradeCommodityMarketInfoRepository")
    public CommodityMarketInfoRepository commodityMarketInfoRepository(
            MybatisCommodityMarketInfoRepository mybatisCommodityMarketInfoRepository,
            MybatisCommodityMarketInfoEntityMapper mybatisCommodityMarketInfoEntityMapper) {
        return new CommodityMarketInfoRepository(mybatisCommodityMarketInfoRepository, mybatisCommodityMarketInfoEntityMapper);
    }
    
    @Bean(name = "tradeCommodityRepository")
    public CommodityRepository commodityRepository(
            MybatisCommodityRepository mybatisCommodityRepository,
            MybatisCommodityEntityMapper mybatisCommodityEntityMapper) {
        return new CommodityRepository(mybatisCommodityEntityMapper, mybatisCommodityRepository);
    }
    
    @Bean(name = "tradeLocateCommodityRepository")
    public LocateCommodityRepository locateCommodityRepository(
            MybatisLocateCommodityRepository mybatisLocateCommodityRepository,
            MybatisLocateCommodityEntityMapper mybatisLocateCommodityEntityMapper,
            MybatisLocateCommodityFilterMapper mybatisLocateCommodityFilterMapper) {
        return new LocateCommodityRepository(mybatisLocateCommodityRepository, mybatisLocateCommodityEntityMapper, mybatisLocateCommodityFilterMapper);
    }
    
    @Bean(name = "tradeLocateTradeRouteRepository")
    public LocateTradeRouteRepository locateTradeRouteRepository(
            MybatisLocateSingleHopTradeRouteRepository mybatisLocateSingleHopTradeRouteRepository,
            MybatisLocateSingleHopTradeFilterMapper mybatisLocateSingleHopTradeFilterMapper,
            MybatisSingleHopEntityMapper mybatisSingleHopEntityMapper,
            MybatisLocateLoopTradeRouteRepository mybatisLocateLoopTradeRouteRepository,
            MybatisLocateLoopTradeFilterMapper mybatisLocateLoopTradeFilterMapper,
            MybatisLoopTradeEntityMapper mybatisLoopTradeEntityMapper) {
        return new LocateTradeRouteRepository(mybatisLocateSingleHopTradeRouteRepository,
                mybatisLocateSingleHopTradeFilterMapper,
                mybatisSingleHopEntityMapper,
                mybatisLocateLoopTradeRouteRepository,
                mybatisLocateLoopTradeFilterMapper,
                mybatisLoopTradeEntityMapper);
    }
    
    @Bean(name = "tradeMarketDatumRepository")
    public MarketDatumRepository marketDatumRepository(
            MybatisMarketDatumRepository mybatisMarketDatumRepository,
            MybatisMarketDatumEntityMapper mybatisMarketDatumEntityMapper) {
        return new MarketDatumRepository(mybatisMarketDatumRepository, mybatisMarketDatumEntityMapper);
    }
    
    @Bean(name = "tradeLatestMarketDatumRepository")
    public LatestMarketDatumRepository latestMarketDatumRepository(
            MybatisLatestMarketDatumRepository mybatisLatestMarketDatumRepository,
            MybatisMarketDatumEntityMapper mybatisMarketDatumEntityMapper) {
        return new LatestMarketDatumRepository(mybatisLatestMarketDatumRepository, mybatisMarketDatumEntityMapper);
    }
    
    @Bean(name = "tradeStationRepository")
    public StationRepository stationRepository(
            MybatisStationRepository mybatisStationRepository,
            MybatisStationEntityMapper mybatisStationEntityMapper,
            MybatisFindStationFilterMapper persistenceMybatisFindStationFilterMapper) {
        return new StationRepository(mybatisStationEntityMapper, mybatisStationRepository, persistenceMybatisFindStationFilterMapper);
    }
    
    @Bean(name = "tradeSystemRepository")
    public SystemRepository systemRepository(
            MybatisSystemRepository mybatisSystemRepository,
            MybatisFindSystemFilterMapper mybatisFindSystemFilterMapper,
            MybatisSystemEntityMapper mybatisSystemEntityMapper) {
        return new SystemRepository(mybatisSystemEntityMapper, mybatisFindSystemFilterMapper, mybatisSystemRepository);
    }
    
    @Bean(name = "tradeValidatedCommodityRepository")
    public ValidatedCommodityRepository validatedCommodityRepository(
            MybatisValidatedCommodityRepository mybatisValidatedCommodityRepository,
            MybatisValidatedCommodityEntityMapper mybatisValidatedCommodityEntityMapper,
            MybatisFindCommodityFilterMapper mybatisFindCommodityFilterMapper) {
        return new ValidatedCommodityRepository(mybatisValidatedCommodityRepository, mybatisValidatedCommodityEntityMapper, mybatisFindCommodityFilterMapper);
    }
    
    @Bean(name = "tradeSystemCoordinateRequestRepository")
    public SystemCoordinateRequestRepository systemCoordinateRequestRepository(
            MybatisSystemCoordinateRequestRepository mybatisSystemCoordinateRequestRepository,
            MybatisSystemDataRequestEntityMapper mybatisSystemDataRequestEntityMapper) {
        return new SystemCoordinateRequestRepository(mybatisSystemCoordinateRequestRepository, mybatisSystemDataRequestEntityMapper);
    }
    
    @Bean(name = "tradeStationRequireOdysseyRequestRepository")
    public StationRequireOdysseyRequestRepository stationRequireOdysseyRequestRepository(
            MybatisStationRequireOdysseyRequestRepository mybatisStationRequireOdysseyRequestRepository,
            MybatisStationDataRequestEntityMapper mybatisStationDataRequestEntityMapper) {
        return new StationRequireOdysseyRequestRepository(mybatisStationRequireOdysseyRequestRepository, mybatisStationDataRequestEntityMapper);
    }
    
    @Bean(name = "tradeSystemEliteIdRequestRepository")
    public SystemEliteIdRequestRepository systemEliteIdRequestRepository(
            MybatisSystemEliteIdRequestRepository mybatisSystemEliteIdRequestRepository,
            MybatisSystemDataRequestEntityMapper mybatisSystemDataRequestEntityMapper) {
        return new SystemEliteIdRequestRepository(mybatisSystemEliteIdRequestRepository, mybatisSystemDataRequestEntityMapper);
    }
    
    @Bean(name = "tradeStationArrivalDistanceRequestRepository")
    public StationArrivalDistanceRequestRepository stationArrivalDistanceRequestRepository(
            MybatisStationArrivalDistanceRequestRepository mybatisStationArrivalDistanceRequestRepository,
            MybatisStationDataRequestEntityMapper mybatisStationDataRequestEntityMapper) {
        return new StationArrivalDistanceRequestRepository(mybatisStationArrivalDistanceRequestRepository, mybatisStationDataRequestEntityMapper);
    }
    
    @Bean(name = "tradeStationPlanetaryRequestRepository")
    public StationPlanetaryRequestRepository stationPlanetaryRequestRepository(
            MybatisStationPlanetaryRequestRepository mybatisStationPlanetaryRequestRepository,
            MybatisStationDataRequestEntityMapper mybatisStationDataRequestEntityMapper) {
        return new StationPlanetaryRequestRepository(mybatisStationPlanetaryRequestRepository, mybatisStationDataRequestEntityMapper);
    }
    
    @Bean(name = "tradeStationLandingPadSizeRequestRepository")
    public StationLandingPadSizeRequestRepository stationLandingPadSizeRequestRepository(
            MybatisStationLandingPadSizeRequestRepository mybatisStationLandingPadSizeRequestRepository,
            MybatisStationDataRequestEntityMapper mybatisStationDataRequestEntityMapper) {
        return new StationLandingPadSizeRequestRepository(mybatisStationLandingPadSizeRequestRepository, mybatisStationDataRequestEntityMapper);
    }
}
