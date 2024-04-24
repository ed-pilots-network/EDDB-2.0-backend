package io.edpn.backend.trade.adapter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.backend.trade.application.domain.Station;
import io.edpn.backend.trade.application.domain.System;
import io.edpn.backend.trade.application.port.incomming.kafka.RequestDataUseCase;
import io.edpn.backend.trade.application.port.outgoing.commodity.CreateOrLoadCommodityPort;
import io.edpn.backend.trade.application.port.outgoing.commoditymarketinfo.GetFullCommodityMarketInfoPort;
import io.edpn.backend.trade.application.port.outgoing.kafka.SendKafkaMessagePort;
import io.edpn.backend.trade.application.port.outgoing.locatecommodity.LocateCommodityByFilterPort;
import io.edpn.backend.trade.application.port.outgoing.locatetraderoute.LocateSingleHopeTradeByFilterPort;
import io.edpn.backend.trade.application.port.outgoing.marketdatum.CreateWhenNotExistsMarketDatumPort;
import io.edpn.backend.trade.application.port.outgoing.marketdatum.createOrUpdateExistingWhenNewerLatestMarketDatumPort;
import io.edpn.backend.trade.application.port.outgoing.station.CreateOrLoadStationPort;
import io.edpn.backend.trade.application.port.outgoing.station.LoadStationsByFilterPort;
import io.edpn.backend.trade.application.port.outgoing.station.LoadStationsBySystemNamePort;
import io.edpn.backend.trade.application.port.outgoing.station.UpdateStationPort;
import io.edpn.backend.trade.application.port.outgoing.stationarrivaldistancerequest.CreateIfNotExistsStationArrivalDistanceRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationarrivaldistancerequest.DeleteStationArrivalDistanceRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationarrivaldistancerequest.ExistsStationArrivalDistanceRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationarrivaldistancerequest.LoadAllStationArrivalDistanceRequestsPort;
import io.edpn.backend.trade.application.port.outgoing.stationlandingpadsizerequest.CreateIfNotExistsStationLandingPadSizeRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationlandingpadsizerequest.DeleteStationLandingPadSizeRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationlandingpadsizerequest.ExistsStationLandingPadSizeRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationlandingpadsizerequest.LoadAllStationLandingPadSizeRequestsPort;
import io.edpn.backend.trade.application.port.outgoing.stationplanetaryrequest.CreateIfNotExistsStationPlanetaryRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationplanetaryrequest.DeleteStationPlanetaryRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationplanetaryrequest.ExistsStationPlanetaryRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationplanetaryrequest.LoadAllStationPlanetaryRequestsPort;
import io.edpn.backend.trade.application.port.outgoing.stationrequireodysseyrequest.CreateIfNotExistsStationRequireOdysseyRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationrequireodysseyrequest.DeleteStationRequireOdysseyRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationrequireodysseyrequest.ExistsStationRequireOdysseyRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationrequireodysseyrequest.LoadAllStationRequireOdysseyRequestsPort;
import io.edpn.backend.trade.application.port.outgoing.system.CreateOrLoadSystemPort;
import io.edpn.backend.trade.application.port.outgoing.system.LoadSystemsByFilterPort;
import io.edpn.backend.trade.application.port.outgoing.system.LoadSystemsByNameContainingPort;
import io.edpn.backend.trade.application.port.outgoing.system.UpdateSystemPort;
import io.edpn.backend.trade.application.port.outgoing.systemcoordinaterequest.CreateIfNotExistsSystemCoordinateRequestPort;
import io.edpn.backend.trade.application.port.outgoing.systemcoordinaterequest.DeleteSystemCoordinateRequestPort;
import io.edpn.backend.trade.application.port.outgoing.systemcoordinaterequest.ExistsSystemCoordinateRequestPort;
import io.edpn.backend.trade.application.port.outgoing.systemcoordinaterequest.LoadAllSystemCoordinateRequestsPort;
import io.edpn.backend.trade.application.port.outgoing.systemeliteidrequest.CreateIfNotExistsSystemEliteIdRequestPort;
import io.edpn.backend.trade.application.port.outgoing.systemeliteidrequest.DeleteSystemEliteIdRequestPort;
import io.edpn.backend.trade.application.port.outgoing.systemeliteidrequest.ExistsSystemEliteIdRequestPort;
import io.edpn.backend.trade.application.port.outgoing.systemeliteidrequest.LoadAllSystemEliteIdRequestsPort;
import io.edpn.backend.trade.application.port.outgoing.validatedcommodity.LoadAllValidatedCommodityPort;
import io.edpn.backend.trade.application.port.outgoing.validatedcommodity.LoadValidatedCommodityByFilterPort;
import io.edpn.backend.trade.application.port.outgoing.validatedcommodity.LoadValidatedCommodityByNamePort;
import io.edpn.backend.trade.application.service.FindCommodityMarketInfoService;
import io.edpn.backend.trade.application.service.FindCommodityService;
import io.edpn.backend.trade.application.service.LocateCommodityService;
import io.edpn.backend.trade.application.service.LocateTradeRouteService;
import io.edpn.backend.trade.application.service.ReceiveCommodityMessageService;
import io.edpn.backend.trade.application.service.StationArrivalDistanceInterModuleCommunicationService;
import io.edpn.backend.trade.application.service.StationControllerService;
import io.edpn.backend.trade.application.service.StationLandingPadSizeInterModuleCommunicationService;
import io.edpn.backend.trade.application.service.StationPlanetaryInterModuleCommunicationService;
import io.edpn.backend.trade.application.service.StationRequireOdysseyInterModuleCommunicationService;
import io.edpn.backend.trade.application.service.SystemControllerService;
import io.edpn.backend.trade.application.service.SystemCoordinateInterModuleCommunicationService;
import io.edpn.backend.trade.application.service.SystemEliteIdInterModuleCommunicationService;
import io.edpn.backend.trade.application.validation.LoadByNameContainingValidator;
import io.edpn.backend.util.IdGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;
import java.util.concurrent.Executor;

@Configuration("TradeServiceConfig")
public class ServiceConfig {

    @Bean(name = "tradeFindCommodityService")
    public FindCommodityService findCommodityService(
            LoadAllValidatedCommodityPort loadAllValidatedCommodityPort,
            LoadValidatedCommodityByNamePort loadValidatedCommodityByNamePort,
            LoadValidatedCommodityByFilterPort loadValidatedCommodityByFilterPort) {
        return new FindCommodityService(loadAllValidatedCommodityPort, loadValidatedCommodityByNamePort, loadValidatedCommodityByFilterPort);
    }

    @Bean(name = "tradeFindCommodityMarketInfoService")
    public FindCommodityMarketInfoService findCommodityMarketInfoService(
            GetFullCommodityMarketInfoPort commodityMarketInfoPort) {
        return new FindCommodityMarketInfoService(commodityMarketInfoPort);
    }

    @Bean(name = "tradeLocateCommodityService")
    public LocateCommodityService locateCommodityService(
            LocateCommodityByFilterPort locateCommodityByFilterPort) {
        return new LocateCommodityService(locateCommodityByFilterPort);
    }
    
    @Bean(name ="tradeStationControllerService")
    public StationControllerService stationControllerService(
            LoadStationsBySystemNamePort loadStationsBySystemNamePort){
    return new StationControllerService(loadStationsBySystemNamePort);
    }
    
    @Bean(name ="tradeSystemControllerService")
    public SystemControllerService systemControllerService(
            LoadSystemsByNameContainingPort loadSystemsByNameContainingPort,
            LoadByNameContainingValidator loadByNameContainingValidator) {
        return new SystemControllerService(loadSystemsByNameContainingPort, loadByNameContainingValidator);
    }
    
    @Bean(name = "tradeLocateTradeRouteService")
    public LocateTradeRouteService locateTradeRouteService(
            LocateSingleHopeTradeByFilterPort locateSingleHopeTradeByFilterPort) {
        return new LocateTradeRouteService(locateSingleHopeTradeByFilterPort);
    }
    
    @Bean(name = "tradeLoadByNameContainingValidator")
    public LoadByNameContainingValidator loadByNameContainingValidator(
            @Value(value = "${exploration.loadbynamecontainingvalidator.min_length:3}") final int minLength,
            @Value(value = "${exploration.loadbynamecontainingvalidator.min_size:1}") final int minSize,
            @Value(value = "${exploration.loadbynamecontainingvalidator.max_size:100}") final int maxSize
    ) {
        return new LoadByNameContainingValidator(
                minLength,
                minSize,
                maxSize);
    }

    @Bean(name = "tradeReceiveCommodityMessageUsecase")
    public ReceiveCommodityMessageService receiveCommodityMessageService(
            @Qualifier("tradeIdGenerator") IdGenerator idGenerator,
            CreateOrLoadSystemPort createOrLoadSystemPort,
            CreateOrLoadStationPort createOrLoadStationPort,
            CreateOrLoadCommodityPort createOrLoadCommodityPort,
            CreateWhenNotExistsMarketDatumPort createWhenNotExistsMarketDatumPort,
            createOrUpdateExistingWhenNewerLatestMarketDatumPort createOrUpdateOnConflictWhenNewerLatestMarketDatumPort,
            UpdateStationPort updateStationPort,
            List<RequestDataUseCase<Station>> stationRequestDataServices,
            List<RequestDataUseCase<System>> systemRequestDataService) {
        return new ReceiveCommodityMessageService(idGenerator, createOrLoadSystemPort, createOrLoadStationPort, createOrLoadCommodityPort, createWhenNotExistsMarketDatumPort, createOrUpdateOnConflictWhenNewerLatestMarketDatumPort, updateStationPort, stationRequestDataServices, systemRequestDataService);
    }


    @Bean(name = "tradeStationArrivalDistanceInterModuleCommunicationService")
    public StationArrivalDistanceInterModuleCommunicationService stationArrivalDistanceInterModuleCommunicationService(
            @Qualifier("tradeIdGenerator") IdGenerator idGenerator,
            LoadStationsByFilterPort loadStationsByFilterPort,
            LoadAllStationArrivalDistanceRequestsPort loadAllStationArrivalDistanceRequestsPort,
            CreateOrLoadSystemPort createOrLoadSystemPort,
            CreateOrLoadStationPort createOrLoadStationPort,
            ExistsStationArrivalDistanceRequestPort existsStationArrivalDistanceRequestPort,
            CreateIfNotExistsStationArrivalDistanceRequestPort createIfNotExistsStationArrivalDistanceRequestPort,
            DeleteStationArrivalDistanceRequestPort deleteStationArrivalDistanceRequestPort,
            UpdateStationPort updateStationPort,
            SendKafkaMessagePort sendKafkaMessagePort,
            @Qualifier("tradeRetryTemplate") RetryTemplate retryTemplate,
            @Qualifier("virtualThreadPerTaskExecutor") Executor executor,
            ObjectMapper objectMapper) {
        return new StationArrivalDistanceInterModuleCommunicationService(
                idGenerator,
                loadStationsByFilterPort,
                loadAllStationArrivalDistanceRequestsPort,
                createOrLoadSystemPort,
                createOrLoadStationPort,
                existsStationArrivalDistanceRequestPort,
                createIfNotExistsStationArrivalDistanceRequestPort,
                deleteStationArrivalDistanceRequestPort,
                updateStationPort,
                sendKafkaMessagePort,
                retryTemplate,
                executor,
                objectMapper);
    }

    @Bean(name = "tradeSystemCoordinateInterModuleCommunicationService")
    public SystemCoordinateInterModuleCommunicationService systemCoordinateInterModuleCommunicationService(
            @Qualifier("tradeIdGenerator") IdGenerator idGenerator,
            LoadSystemsByFilterPort loadSystemsByFilterPort,
            LoadAllSystemCoordinateRequestsPort loadAllSystemCoordinateRequestsPort,
            CreateOrLoadSystemPort createOrLoadSystemPort,
            ExistsSystemCoordinateRequestPort existsSystemCoordinateRequestPort,
            CreateIfNotExistsSystemCoordinateRequestPort createIfNotExistsSystemCoordinateRequestPort,
            DeleteSystemCoordinateRequestPort deleteSystemCoordinateRequestPort,
            UpdateSystemPort updateSystemPort,
            SendKafkaMessagePort sendKafkaMessagePort,
            @Qualifier("tradeRetryTemplate") RetryTemplate retryTemplate,
            @Qualifier("virtualThreadPerTaskExecutor") Executor executor,
            ObjectMapper objectMapper
    ) {
        return new SystemCoordinateInterModuleCommunicationService(
                idGenerator,
                loadSystemsByFilterPort,
                loadAllSystemCoordinateRequestsPort,
                createOrLoadSystemPort,
                existsSystemCoordinateRequestPort,
                createIfNotExistsSystemCoordinateRequestPort,
                deleteSystemCoordinateRequestPort,
                updateSystemPort,
                sendKafkaMessagePort,
                retryTemplate,
                executor,
                objectMapper
        );
    }

    @Bean(name = "tradeStationLandingPadSizeInterModuleCommunicationService")
    public StationLandingPadSizeInterModuleCommunicationService stationLandingPadSizeInterModuleCommunicationService(
            @Qualifier("tradeIdGenerator") IdGenerator idGenerator,
            LoadStationsByFilterPort loadStationsByFilterPort,
            LoadAllStationLandingPadSizeRequestsPort loadAllStationLandingPadSizeRequestsPort,
            CreateOrLoadSystemPort createOrLoadSystemPort,
            CreateOrLoadStationPort createOrLoadStationPort,
            ExistsStationLandingPadSizeRequestPort existsStationLandingPadSizeRequestPort,
            CreateIfNotExistsStationLandingPadSizeRequestPort createIfNotExistsStationLandingPadSizeRequestPort,
            DeleteStationLandingPadSizeRequestPort deleteStationLandingPadSizeRequestPort,
            UpdateStationPort updateStationPort,
            SendKafkaMessagePort sendKafkaMessagePort,
            @Qualifier("tradeRetryTemplate") RetryTemplate retryTemplate,
            @Qualifier("virtualThreadPerTaskExecutor") Executor executor,
            ObjectMapper objectMapper
    ) {
        return new StationLandingPadSizeInterModuleCommunicationService(
                idGenerator,
                loadStationsByFilterPort,
                loadAllStationLandingPadSizeRequestsPort,
                createOrLoadSystemPort,
                createOrLoadStationPort,
                existsStationLandingPadSizeRequestPort,
                createIfNotExistsStationLandingPadSizeRequestPort,
                deleteStationLandingPadSizeRequestPort,
                updateStationPort,
                sendKafkaMessagePort,
                retryTemplate,
                executor,
                objectMapper
        );
    }

    @Bean(name = "tradeStationPlanetaryInterModuleCommunicationService")
    public StationPlanetaryInterModuleCommunicationService stationPlanetaryInterModuleCommunicationService(
            @Qualifier("tradeIdGenerator") IdGenerator idGenerator,
            LoadStationsByFilterPort loadStationsByFilterPort,
            LoadAllStationPlanetaryRequestsPort loadAllStationPlanetaryRequestsPort,
            CreateOrLoadSystemPort createOrLoadSystemPort,
            CreateOrLoadStationPort createOrLoadStationPort,
            ExistsStationPlanetaryRequestPort existsStationPlanetaryRequestPort,
            CreateIfNotExistsStationPlanetaryRequestPort createIfNotExistsStationPlanetaryRequestPort,
            DeleteStationPlanetaryRequestPort deleteStationPlanetaryRequestPort,
            UpdateStationPort updateStationPort,
            SendKafkaMessagePort sendKafkaMessagePort,
            @Qualifier("tradeRetryTemplate") RetryTemplate retryTemplate,
            @Qualifier("virtualThreadPerTaskExecutor") Executor executor,
            ObjectMapper objectMapper
    ) {
        return new StationPlanetaryInterModuleCommunicationService(
                idGenerator,
                loadStationsByFilterPort,
                loadAllStationPlanetaryRequestsPort,
                createOrLoadSystemPort,
                createOrLoadStationPort,
                existsStationPlanetaryRequestPort,
                createIfNotExistsStationPlanetaryRequestPort,
                deleteStationPlanetaryRequestPort,
                updateStationPort,
                sendKafkaMessagePort,
                retryTemplate,
                executor,
                objectMapper
        );
    }

    @Bean(name = "tradeStationRequireOdysseyInterModuleCommunicationService")
    public StationRequireOdysseyInterModuleCommunicationService stationRequireOdysseyInterModuleCommunicationService(
            @Qualifier("tradeIdGenerator") IdGenerator idGenerator,
            LoadStationsByFilterPort loadStationsByFilterPort,
            LoadAllStationRequireOdysseyRequestsPort loadAllStationRequireOdysseyRequestsPort,
            CreateOrLoadSystemPort createOrLoadSystemPort,
            CreateOrLoadStationPort createOrLoadStationPort,
            ExistsStationRequireOdysseyRequestPort existsStationRequireOdysseyRequestPort,
            CreateIfNotExistsStationRequireOdysseyRequestPort createIfNotExistsStationRequireOdysseyRequestPort,
            DeleteStationRequireOdysseyRequestPort deleteStationRequireOdysseyRequestPort,
            UpdateStationPort updateStationPort,
            SendKafkaMessagePort sendKafkaMessagePort,
            @Qualifier("tradeRetryTemplate") RetryTemplate retryTemplate,
            @Qualifier("virtualThreadPerTaskExecutor") Executor executor,
            ObjectMapper objectMapper
    ) {
        return new StationRequireOdysseyInterModuleCommunicationService(
                idGenerator,
                loadStationsByFilterPort,
                loadAllStationRequireOdysseyRequestsPort,
                createOrLoadSystemPort,
                createOrLoadStationPort,
                existsStationRequireOdysseyRequestPort,
                createIfNotExistsStationRequireOdysseyRequestPort,
                deleteStationRequireOdysseyRequestPort,
                updateStationPort,
                sendKafkaMessagePort,
                retryTemplate,
                executor,
                objectMapper
        );
    }

    @Bean(name = "tradeSystemEliteIdInterModuleCommunicationService")
    public SystemEliteIdInterModuleCommunicationService systemEliteIdInterModuleCommunicationService(
            @Qualifier("tradeIdGenerator") IdGenerator idGenerator,
            LoadSystemsByFilterPort loadSystemsByFilterPort,
            LoadAllSystemEliteIdRequestsPort loadAllSystemEliteIdRequestsPort,
            CreateOrLoadSystemPort createOrLoadSystemPort,
            ExistsSystemEliteIdRequestPort existsSystemEliteIdRequestPort,
            CreateIfNotExistsSystemEliteIdRequestPort createIfNotExistsSystemEliteIdRequestPort,
            DeleteSystemEliteIdRequestPort deleteSystemEliteIdRequestPort,
            UpdateSystemPort updateSystemPort,
            SendKafkaMessagePort sendKafkaMessagePort,
            @Qualifier("tradeRetryTemplate") RetryTemplate retryTemplate,
            @Qualifier("virtualThreadPerTaskExecutor") Executor executor,
            ObjectMapper objectMapper
    ) {
        return new SystemEliteIdInterModuleCommunicationService(
                idGenerator,
                loadSystemsByFilterPort,
                loadAllSystemEliteIdRequestsPort,
                createOrLoadSystemPort,
                existsSystemEliteIdRequestPort,
                createIfNotExistsSystemEliteIdRequestPort,
                deleteSystemEliteIdRequestPort,
                updateSystemPort,
                sendKafkaMessagePort,
                retryTemplate,
                executor,
                objectMapper
        );
    }
}
