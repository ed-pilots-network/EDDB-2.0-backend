package io.edpn.backend.trade.adapter.config;

import io.edpn.backend.trade.adapter.persistence.CommodityMarketInfoRepository;
import io.edpn.backend.trade.adapter.persistence.CommodityRepository;
import io.edpn.backend.trade.adapter.persistence.LocateCommodityRepository;
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
import io.edpn.backend.trade.adapter.persistence.entity.MybatisCommodityEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisCommodityMarketInfoEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisLocateCommodityEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisStationEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisSystemEntity;
import io.edpn.backend.trade.adapter.persistence.entity.MybatisValidatedCommodityEntity;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisPersistenceFindCommodityFilterMapper;
import io.edpn.backend.trade.adapter.persistence.filter.mapper.MybatisPersistenceLocateCommodityFilterMapper;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisCommodityMarketInfoRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisCommodityRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateCommodityRepository;
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
import io.edpn.backend.trade.application.dto.persistence.entity.mapper.CommodityEntityMapper;
import io.edpn.backend.trade.application.dto.persistence.entity.mapper.CommodityMarketInfoEntityMapper;
import io.edpn.backend.trade.application.dto.persistence.entity.mapper.LocateCommodityEntityMapper;
import io.edpn.backend.trade.application.dto.persistence.entity.mapper.StationDataRequestEntityMapper;
import io.edpn.backend.trade.application.dto.persistence.entity.mapper.StationEntityMapper;
import io.edpn.backend.trade.application.dto.persistence.entity.mapper.SystemDataRequestEntityMapper;
import io.edpn.backend.trade.application.dto.persistence.entity.mapper.SystemEntityMapper;
import io.edpn.backend.trade.application.dto.persistence.entity.mapper.ValidatedCommodityEntityMapper;
import io.edpn.backend.trade.application.dto.persistence.filter.mapper.PersistenceFindStationFilterMapper;
import io.edpn.backend.trade.application.dto.persistence.filter.mapper.PersistenceFindSystemFilterMapper;
import io.edpn.backend.util.IdGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("TradeRepositoryConfig")
public class RepositoryConfig {

    @Bean(name = "tradeCommodityMarketInfoRepository")
    public CommodityMarketInfoRepository commodityMarketInfoRepository(
            MybatisCommodityMarketInfoRepository mybatisCommodityMarketInfoRepository,
            CommodityMarketInfoEntityMapper<MybatisCommodityMarketInfoEntity> mybatisCommodityMarketInfoEntityMapper) {
        return new CommodityMarketInfoRepository(mybatisCommodityMarketInfoRepository, mybatisCommodityMarketInfoEntityMapper);
    }

    @Bean(name = "tradeCommodityRepository")
    public CommodityRepository commodityRepository(
            @Qualifier("tradeIdGenerator") IdGenerator idGenerator,
            MybatisCommodityRepository mybatisCommodityRepository,
            CommodityEntityMapper<MybatisCommodityEntity> mybatisCommodityEntityMapper) {
        return new CommodityRepository(idGenerator, mybatisCommodityEntityMapper, mybatisCommodityRepository);
    }

    @Bean(name = "tradeLocateCommodityRepository")
    public LocateCommodityRepository locateCommodityRepository(
            MybatisLocateCommodityRepository mybatisLocateCommodityRepository,
            LocateCommodityEntityMapper<MybatisLocateCommodityEntity> mybatisLocateCommodityEntityMapper,
            MybatisPersistenceLocateCommodityFilterMapper mybatisPersistenceLocateCommodityFilterMapper) {
        return new LocateCommodityRepository(mybatisLocateCommodityRepository, mybatisLocateCommodityEntityMapper, mybatisPersistenceLocateCommodityFilterMapper);
    }

    @Bean(name = "tradeMarketDatumRepository")
    public MarketDatumRepository marketDatumRepository(
            MybatisMarketDatumRepository mybatisMarketDatumRepository) {
        return new MarketDatumRepository(mybatisMarketDatumRepository);
    }

    @Bean(name = "tradeStationRepository")
    public StationRepository stationRepository(
            @Qualifier("tradeIdGenerator") IdGenerator idGenerator,
            MybatisStationRepository mybatisStationRepository,
            StationEntityMapper<MybatisStationEntity> mybatisStationEntityMapper,
            MybatisMarketDatumRepository mybatisMarketDatumRepository,
            PersistenceFindStationFilterMapper persistenceFindStationFilterMapper) {
        return new StationRepository(idGenerator, mybatisStationEntityMapper, mybatisStationRepository, mybatisMarketDatumRepository, persistenceFindStationFilterMapper);
    }

    @Bean(name = "tradeSystemRepository")
    public SystemRepository systemRepository(
            @Qualifier("tradeIdGenerator") IdGenerator idGenerator,
            MybatisSystemRepository mybatisSystemRepository,
            PersistenceFindSystemFilterMapper mybatisPersistenceFindSystemFilterMapper,
            SystemEntityMapper<MybatisSystemEntity> mybatisSystemEntityMapper) {
        return new SystemRepository(idGenerator, mybatisSystemEntityMapper, mybatisPersistenceFindSystemFilterMapper, mybatisSystemRepository);
    }

    @Bean(name = "tradeValidatedCommodityRepository")
    public ValidatedCommodityRepository validatedCommodityRepository(
            MybatisValidatedCommodityRepository mybatisValidatedCommodityRepository,
            ValidatedCommodityEntityMapper<MybatisValidatedCommodityEntity> mybatisValidatedCommodityEntityMapper,
            MybatisPersistenceFindCommodityFilterMapper mybatisPersistenceFindCommodityFilterMapper) {
        return new ValidatedCommodityRepository(mybatisValidatedCommodityRepository, mybatisValidatedCommodityEntityMapper, mybatisPersistenceFindCommodityFilterMapper);
    }

    @Bean(name = "tradeSystemCoordinateRequestRepository")
    public SystemCoordinateRequestRepository systemCoordinateRequestRepository(
            MybatisSystemCoordinateRequestRepository mybatisSystemCoordinateRequestRepository,
            SystemDataRequestEntityMapper systemDataRequestEntityMapper) {
        return new SystemCoordinateRequestRepository(mybatisSystemCoordinateRequestRepository, systemDataRequestEntityMapper);
    }

    @Bean(name = "tradeStationRequireOdysseyRequestRepository")
    public StationRequireOdysseyRequestRepository stationRequireOdysseyRequestRepository(
            MybatisStationRequireOdysseyRequestRepository mybatisStationRequireOdysseyRequestRepository,
            StationDataRequestEntityMapper stationDataRequestEntityMapper) {
        return new StationRequireOdysseyRequestRepository(mybatisStationRequireOdysseyRequestRepository, stationDataRequestEntityMapper);
    }

    @Bean(name = "tradeSystemEliteIdRequestRepository")
    public SystemEliteIdRequestRepository systemEliteIdRequestRepository(
            MybatisSystemEliteIdRequestRepository mybatisSystemEliteIdRequestRepository,
            SystemDataRequestEntityMapper systemDataRequestEntityMapper) {
        return new SystemEliteIdRequestRepository(mybatisSystemEliteIdRequestRepository, systemDataRequestEntityMapper);
    }

    @Bean(name = "tradeStationArrivalDistanceRequestRepository")
    public StationArrivalDistanceRequestRepository stationArrivalDistanceRequestRepository(
            MybatisStationArrivalDistanceRequestRepository mybatisStationArrivalDistanceRequestRepository,
            StationDataRequestEntityMapper stationDataRequestEntityMapper) {
        return new StationArrivalDistanceRequestRepository(mybatisStationArrivalDistanceRequestRepository, stationDataRequestEntityMapper);
    }

    @Bean(name = "tradeStationPlanetaryRequestRepository")
    public StationPlanetaryRequestRepository stationPlanetaryRequestRepository(
            MybatisStationPlanetaryRequestRepository mybatisStationPlanetaryRequestRepository,
            StationDataRequestEntityMapper stationDataRequestEntityMapper) {
        return new StationPlanetaryRequestRepository(mybatisStationPlanetaryRequestRepository, stationDataRequestEntityMapper);
    }

    @Bean(name = "tradeStationLandingPadSizeRequestRepository")
    public StationLandingPadSizeRequestRepository stationLandingPadSizeRequestRepository(
            MybatisStationLandingPadSizeRequestRepository mybatisStationLandingPadSizeRequestRepository,
            StationDataRequestEntityMapper stationDataRequestEntityMapper) {
        return new StationLandingPadSizeRequestRepository(mybatisStationLandingPadSizeRequestRepository, stationDataRequestEntityMapper);
    }
}
