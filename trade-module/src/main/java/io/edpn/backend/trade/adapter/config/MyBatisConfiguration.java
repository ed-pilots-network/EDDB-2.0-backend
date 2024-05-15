package io.edpn.backend.trade.adapter.config;

import io.edpn.backend.mybatisutil.StringListToArrayTypeHandler;
import io.edpn.backend.mybatisutil.StringTrimmingTypeHandler;
import io.edpn.backend.mybatisutil.UuidTypeHandler;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisCommodityMarketInfoRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisCommodityRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLatestMarketDatumRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateCommodityRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateLoopTradeRouteRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisLocateSingleHopTradeRouteRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisMarketDatumRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisMessageRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationLandingPadSizeRequestRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisSystemCoordinateRequestRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationPlanetaryRequestRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationRequireOdysseyRequestRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisSystemEliteIdRequestRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisStationArrivalDistanceRequestRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisSystemRepository;
import io.edpn.backend.trade.adapter.persistence.repository.MybatisValidatedCommodityRepository;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration("TradeModuleMyBatisConfiguration")
public class MyBatisConfiguration {

    @Bean(name = "tradeSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("tradeDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setTypeHandlers(new UuidTypeHandler(), new StringTrimmingTypeHandler(), new StringListToArrayTypeHandler());

        return sessionFactoryBean.getObject();
    }

    @Bean(name = "tradeMybatisCommodityMarketInfoRepository")
    public MapperFactoryBean<MybatisCommodityMarketInfoRepository> mybatisCommodityMarketInfoRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisCommodityMarketInfoRepository> factoryBean = new MapperFactoryBean<>(MybatisCommodityMarketInfoRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

    @Bean(name = "tradeMybatisCommodityRepository")
    public MapperFactoryBean<MybatisCommodityRepository> mybatisCommodityRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisCommodityRepository> factoryBean = new MapperFactoryBean<>(MybatisCommodityRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

    @Bean(name = "tradeMybatisLocateCommodityRepository")
    public MapperFactoryBean<MybatisLocateCommodityRepository> mybatisLocateCommodityRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisLocateCommodityRepository> factoryBean = new MapperFactoryBean<>(MybatisLocateCommodityRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }
    
    @Bean(name = "tradeMybatisLocateSingleHopTradeRouteRepository")
    public MapperFactoryBean<MybatisLocateSingleHopTradeRouteRepository> mybatisLocateSingleHopTradeRouteRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisLocateSingleHopTradeRouteRepository> factoryBean = new MapperFactoryBean<>(MybatisLocateSingleHopTradeRouteRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }
    
    @Bean(name = "tradeMybatisLocateLoopTradeRouteRepository")
    public MapperFactoryBean<MybatisLocateLoopTradeRouteRepository> mybatisLocateLoopTradeRouteRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        MapperFactoryBean<MybatisLocateLoopTradeRouteRepository> factoryBean = new MapperFactoryBean<>(MybatisLocateLoopTradeRouteRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }
    
    @Bean(name = "tradeMybatisLatestMarketDatumRepository")
    public MapperFactoryBean<MybatisLatestMarketDatumRepository> mybatisLatestMarketDatumRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisLatestMarketDatumRepository> factoryBean = new MapperFactoryBean<>(MybatisLatestMarketDatumRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

    @Bean(name = "tradeMybatisMarketDatumRepository")
    public MapperFactoryBean<MybatisMarketDatumRepository> mybatisMarketDatumRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisMarketDatumRepository> factoryBean = new MapperFactoryBean<>(MybatisMarketDatumRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

    @Bean(name = "tradeMybatisRequestDataMessageRepository")
    public MapperFactoryBean<MybatisMessageRepository> mybatisRequestDataMessageRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisMessageRepository> factoryBean = new MapperFactoryBean<>(MybatisMessageRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

    @Bean(name = "tradeMybatisStationRepository")
    public MapperFactoryBean<MybatisStationRepository> mybatisStationRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisStationRepository> factoryBean = new MapperFactoryBean<>(MybatisStationRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

    @Bean(name = "tradeMybatisSystemRepository")
    public MapperFactoryBean<MybatisSystemRepository> mybatisSystemRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisSystemRepository> factoryBean = new MapperFactoryBean<>(MybatisSystemRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

    @Bean(name = "tradeMybatisValidatedCommodityRepository")
    public MapperFactoryBean<MybatisValidatedCommodityRepository> mybatisValidatedCommodityRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisValidatedCommodityRepository> factoryBean = new MapperFactoryBean<>(MybatisValidatedCommodityRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

    @Bean(name = "tradeMybatisSystemCoordinateRequestRepository")
    public MapperFactoryBean<MybatisSystemCoordinateRequestRepository> mybatisSystemCoordinateRequestRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisSystemCoordinateRequestRepository> factoryBean = new MapperFactoryBean<>(MybatisSystemCoordinateRequestRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

    @Bean(name = "tradeMybatisStationRequireOdysseyRequestRepository")
    public MapperFactoryBean<MybatisStationRequireOdysseyRequestRepository> mybatisStationRequireOdysseyRequestRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisStationRequireOdysseyRequestRepository> factoryBean = new MapperFactoryBean<>(MybatisStationRequireOdysseyRequestRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

  @Bean(name = "tradeMybatisSystemEliteIdRequestRepository")
    public MapperFactoryBean<MybatisSystemEliteIdRequestRepository> mybatisSystemEliteIdRequestRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisSystemEliteIdRequestRepository> factoryBean = new MapperFactoryBean<>(MybatisSystemEliteIdRequestRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

    @Bean(name = "tradeMybatisStationArrivalDistanceRequestRepository")
    public MapperFactoryBean<MybatisStationArrivalDistanceRequestRepository> mybatisStationArrivalDistanceRequestRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisStationArrivalDistanceRequestRepository> factoryBean = new MapperFactoryBean<>(MybatisStationArrivalDistanceRequestRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }
 
    @Bean(name = "tradeMybatisStationPlanetaryRequestRepository")
    public MapperFactoryBean<MybatisStationPlanetaryRequestRepository> mybatisStationPlanetaryRequestRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisStationPlanetaryRequestRepository> factoryBean = new MapperFactoryBean<>(MybatisStationPlanetaryRequestRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }
  
    @Bean(name = "tradeMybatisStationLandingPadSizeRequestRepository")
    public MapperFactoryBean<MybatisStationLandingPadSizeRequestRepository> mybatisStationLandingPadSizeRequestRepository(@Qualifier("tradeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<MybatisStationLandingPadSizeRequestRepository> factoryBean = new MapperFactoryBean<>(MybatisStationLandingPadSizeRequestRepository.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }
}
