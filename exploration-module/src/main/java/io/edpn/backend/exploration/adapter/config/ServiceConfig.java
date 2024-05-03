package io.edpn.backend.exploration.adapter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.backend.exploration.application.port.outgoing.body.SaveOrUpdateBodyPort;
import io.edpn.backend.exploration.application.port.outgoing.message.SendMessagePort;
import io.edpn.backend.exploration.application.port.outgoing.ring.SaveOrUpdateRingPort;
import io.edpn.backend.exploration.application.port.outgoing.star.SaveOrUpdateStarPort;
import io.edpn.backend.exploration.application.port.outgoing.station.LoadStationNamesBySystemNamePort;
import io.edpn.backend.exploration.application.port.outgoing.station.LoadStationPort;
import io.edpn.backend.exploration.application.port.outgoing.station.SaveOrUpdateStationPort;
import io.edpn.backend.exploration.application.port.outgoing.stationarrivaldistancerequest.CreateIfNotExistsStationArrivalDistanceRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.stationarrivaldistancerequest.DeleteStationArrivalDistanceRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.stationarrivaldistancerequest.LoadAllStationArrivalDistanceRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.stationarrivaldistancerequest.LoadStationArrivalDistanceRequestByIdentifierPort;
import io.edpn.backend.exploration.application.port.outgoing.stationarrivaldistancerequest.StationArrivalDistanceResponseSender;
import io.edpn.backend.exploration.application.port.outgoing.stationmaxlandingpadsizerequest.CreateIfNotExistsStationMaxLandingPadSizeRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.stationmaxlandingpadsizerequest.DeleteStationMaxLandingPadSizeRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.stationmaxlandingpadsizerequest.LoadAllStationMaxLandingPadSizeRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.stationmaxlandingpadsizerequest.LoadStationMaxLandingPadSizeRequestByIdentifierPort;
import io.edpn.backend.exploration.application.port.outgoing.stationmaxlandingpadsizerequest.StationMaxLandingPadSizeResponseSender;
import io.edpn.backend.exploration.application.port.outgoing.system.LoadSystemPort;
import io.edpn.backend.exploration.application.port.outgoing.system.LoadSystemsByNameContainingPort;
import io.edpn.backend.exploration.application.port.outgoing.system.SaveOrUpdateSystemPort;
import io.edpn.backend.exploration.application.port.outgoing.systemcoordinaterequest.CreateIfNotExistsSystemCoordinateRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.systemcoordinaterequest.DeleteSystemCoordinateRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.systemcoordinaterequest.LoadAllSystemCoordinateRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.systemcoordinaterequest.LoadSystemCoordinateRequestByIdentifierPort;
import io.edpn.backend.exploration.application.port.outgoing.systemcoordinaterequest.SystemCoordinatesResponseSender;
import io.edpn.backend.exploration.application.port.outgoing.systemeliteidrequest.CreateIfNotExistsSystemEliteIdRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.systemeliteidrequest.DeleteSystemEliteIdRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.systemeliteidrequest.LoadAllSystemEliteIdRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.systemeliteidrequest.LoadSystemEliteIdRequestByIdentifierPort;
import io.edpn.backend.exploration.application.port.outgoing.systemeliteidrequest.SystemEliteIdResponseSender;
import io.edpn.backend.exploration.application.service.ReceiveJournalDockedService;
import io.edpn.backend.exploration.application.service.ReceiveJournalScanService;
import io.edpn.backend.exploration.application.service.ReceiveNavRouteService;
import io.edpn.backend.exploration.application.service.StationArrivalDistanceInterModuleCommunicationService;
import io.edpn.backend.exploration.application.service.StationArrivalDistanceResponseSenderService;
import io.edpn.backend.exploration.application.service.StationMaxLandingPadSizeInterModuleCommunicationService;
import io.edpn.backend.exploration.application.service.StationMaxLandingPadSizeResponseSenderService;
import io.edpn.backend.exploration.application.service.SystemControllerService;
import io.edpn.backend.exploration.application.service.SystemCoordinateInterModuleCommunicationService;
import io.edpn.backend.exploration.application.service.SystemCoordinatesResponseSenderService;
import io.edpn.backend.exploration.application.service.SystemEliteIdInterModuleCommunicationService;
import io.edpn.backend.exploration.application.service.SystemEliteIdResponseSenderService;
import io.edpn.backend.exploration.application.validation.LoadByNameContainingValidator;
import io.edpn.backend.util.IdGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.ExecutorService;

@Configuration("ExplorationServiceConfig")
public class ServiceConfig {

    @Bean(name = "explorationReceiveNavRouteService")
    public ReceiveNavRouteService receiveNavRouteService(
            @Qualifier("explorationIdGenerator") IdGenerator idGenerator,
            SaveOrUpdateSystemPort saveOrUpdateSystemPort,
            SystemCoordinatesResponseSender systemCoordinatesResponseSender,
            SystemEliteIdResponseSender systemEliteIdResponseSender,
            @Qualifier("virtualThreadPerTaskExecutor") ExecutorService executorService
    ) {
        return new ReceiveNavRouteService(
                idGenerator,
                saveOrUpdateSystemPort,
                systemCoordinatesResponseSender,
                systemEliteIdResponseSender,
                executorService);
    }

    @Bean(name = "explorationReceiveJournalScanService")
    public ReceiveJournalScanService receiveJournalScanService(
            @Qualifier("explorationIdGenerator") IdGenerator idGenerator,
            SaveOrUpdateBodyPort saveOrUpdateBodyPort,
            SaveOrUpdateRingPort saveOrUpdateRingPort,
            SaveOrUpdateStarPort saveOrUpdateStarPort,
            SaveOrUpdateSystemPort saveOrUpdateSystemPort
    ) {
        return new ReceiveJournalScanService(
                idGenerator,
                saveOrUpdateBodyPort,
                saveOrUpdateRingPort,
                saveOrUpdateStarPort,
                saveOrUpdateSystemPort);
    }

    @Bean(name = "explorationReceiveJournalDockedService")
    public ReceiveJournalDockedService receiveJournalDockedService(
            @Qualifier("explorationIdGenerator") IdGenerator idGenerator,

            SystemCoordinatesResponseSender systemCoordinatesResponseSender,
            SystemEliteIdResponseSender systemEliteIdResponseSender,
            StationMaxLandingPadSizeResponseSender stationMaxLandingPadSizeResponseSender,
            SaveOrUpdateStationPort saveOrUpdateStationPort,
            SaveOrUpdateSystemPort saveOrUpdateSystemPort,
            @Qualifier("virtualThreadPerTaskExecutor") ExecutorService executorService
    ) {
        return new ReceiveJournalDockedService(
                idGenerator,
                systemCoordinatesResponseSender,
                systemEliteIdResponseSender,
                stationMaxLandingPadSizeResponseSender,
                saveOrUpdateStationPort,
                saveOrUpdateSystemPort,
                executorService);
    }

    @Bean(name = "explorationSystemCoordinateInterModuleCommunicationService")
    public SystemCoordinateInterModuleCommunicationService systemCoordinateInterModuleCommunicationService(
            LoadAllSystemCoordinateRequestPort loadAllSystemCoordinateRequestPort,
            CreateIfNotExistsSystemCoordinateRequestPort createIfNotExistsSystemCoordinateRequestPort,
            LoadSystemPort loadSystemPort,
            SystemCoordinatesResponseSender systemCoordinatesResponseSender,
            @Qualifier("virtualThreadPerTaskExecutor") ExecutorService executorService
    ) {
        return new SystemCoordinateInterModuleCommunicationService(
                loadAllSystemCoordinateRequestPort,
                createIfNotExistsSystemCoordinateRequestPort,
                loadSystemPort,
                systemCoordinatesResponseSender,
                executorService
        );
    }

    @Bean(name = "explorationSystemEliteIdInterModuleCommunicationService")
    public SystemEliteIdInterModuleCommunicationService systemEliteIdInterModuleCommunicationService(
            LoadAllSystemEliteIdRequestPort loadAllSystemEliteIdRequestPort,
            CreateIfNotExistsSystemEliteIdRequestPort createIfNotExistsSystemEliteIdRequestPort,
            LoadSystemPort loadSystemPort,
            SystemEliteIdResponseSender systemEliteIdResponseSender,
            @Qualifier("virtualThreadPerTaskExecutor") ExecutorService executorService
    ) {
        return new SystemEliteIdInterModuleCommunicationService(
                loadAllSystemEliteIdRequestPort,
                createIfNotExistsSystemEliteIdRequestPort,
                loadSystemPort,
                systemEliteIdResponseSender,
                executorService
        );
    }

    @Bean(name = "explorationStationArrivalDistanceInterModuleCommunicationService")
    public StationArrivalDistanceInterModuleCommunicationService stationArrivalDistanceInterModuleCommunicationService(
            LoadAllStationArrivalDistanceRequestPort loadAllStationArrivalDistanceRequestPort,
            CreateIfNotExistsStationArrivalDistanceRequestPort createIfNotExistsStationArrivalDistanceRequestPort,
            LoadStationPort loadStationPort,
            StationArrivalDistanceResponseSender stationArrivalDistanceResponseSender,
            @Qualifier("virtualThreadPerTaskExecutor") ExecutorService executorService
    ) {
        return new StationArrivalDistanceInterModuleCommunicationService(
                loadAllStationArrivalDistanceRequestPort,
                createIfNotExistsStationArrivalDistanceRequestPort,
                loadStationPort,
                stationArrivalDistanceResponseSender,
                executorService
        );
    }

    @Bean(name = "explorationStationMaxLandingPadSizeInterModuleCommunicationService")
    public StationMaxLandingPadSizeInterModuleCommunicationService stationMaxLandingPadSizeInterModuleCommunicationService(
            LoadAllStationMaxLandingPadSizeRequestPort loadAllStationMaxLandingPadSizeRequestPort,
            CreateIfNotExistsStationMaxLandingPadSizeRequestPort createIfNotExistsStationMaxLandingPadSizeRequestPort,
            LoadStationPort loadStationPort,
            StationMaxLandingPadSizeResponseSender stationMaxLandingPadSizeResponseSender,
            @Qualifier("virtualThreadPerTaskExecutor") ExecutorService executorService
    ) {
        return new StationMaxLandingPadSizeInterModuleCommunicationService(
                loadAllStationMaxLandingPadSizeRequestPort,
                createIfNotExistsStationMaxLandingPadSizeRequestPort,
                loadStationPort,
                stationMaxLandingPadSizeResponseSender,
                executorService
        );
    }

    @Bean(name = "explorationLoadByNameContainingValidator")
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


    @Bean(name = "explorationSystemControllerService")
    public SystemControllerService systemControllerService(
            LoadSystemsByNameContainingPort loadSystemsByNameContainingPort,
            LoadByNameContainingValidator loadByNameContainingValidator,
            LoadStationNamesBySystemNamePort loadStationNamesBySystemNamePort) {
        return new SystemControllerService(
                loadSystemsByNameContainingPort,
                loadByNameContainingValidator,
                loadStationNamesBySystemNamePort);
    }

    @Bean("explorationSystemCoordinatesResponseSender")
    public SystemCoordinatesResponseSender systemCoordinatesResponseSender(
            LoadSystemPort loadSystemPort,
            LoadSystemCoordinateRequestByIdentifierPort loadSystemCoordinateRequestBySystemNamePort,
            DeleteSystemCoordinateRequestPort deleteSystemCoordinateRequestPort,
            SendMessagePort sendMessagePort,
            ObjectMapper objectMapper,
            @Qualifier("explorationRetryTemplate") RetryTemplate retryTemplate,
            @Qualifier("virtualThreadPerTaskExecutor") ExecutorService executorService
    ) {
        return new SystemCoordinatesResponseSenderService(
                loadSystemPort,
                loadSystemCoordinateRequestBySystemNamePort,
                deleteSystemCoordinateRequestPort,
                sendMessagePort,
                objectMapper,
                retryTemplate,
                executorService
        );
    }

    @Bean("explorationSystemEliteIdResponseSender")
    public SystemEliteIdResponseSender systemEliteIdResponseSender(
            LoadSystemPort loadSystemPort,
            LoadSystemEliteIdRequestByIdentifierPort loadSystemEliteIdRequestBySystemNamePort,
            DeleteSystemEliteIdRequestPort deleteSystemEliteIdRequestPort,
            SendMessagePort sendMessagePort,
            ObjectMapper objectMapper,
            @Qualifier("explorationRetryTemplate") RetryTemplate retryTemplate,
            @Qualifier("virtualThreadPerTaskExecutor") ExecutorService executorService
    ) {
        return new SystemEliteIdResponseSenderService(
                loadSystemPort,
                loadSystemEliteIdRequestBySystemNamePort,
                deleteSystemEliteIdRequestPort,
                sendMessagePort,
                objectMapper,
                retryTemplate,
                executorService
        );
    }

    @Bean("explorationStationArrivalDistanceResponseSender")
    public StationArrivalDistanceResponseSender stationArrivalDistanceResponseSender(
            LoadStationPort loadStationPort,
            LoadStationArrivalDistanceRequestByIdentifierPort loadStationArrivalDistanceRequestByIdentifierPort,
            DeleteStationArrivalDistanceRequestPort deleteStationArrivalDistanceRequestPort,
            SendMessagePort sendMessagePort,
            ObjectMapper objectMapper,
            @Qualifier("explorationRetryTemplate") RetryTemplate retryTemplate,
            @Qualifier("virtualThreadPerTaskExecutor") ExecutorService executorService
    ) {
        return new StationArrivalDistanceResponseSenderService(
                loadStationPort,
                loadStationArrivalDistanceRequestByIdentifierPort,
                deleteStationArrivalDistanceRequestPort,
                sendMessagePort,
                objectMapper,
                retryTemplate,
                executorService
        );
    }

    @Bean("explorationStationMaxLandingPadSizeResponseSender")
    public StationMaxLandingPadSizeResponseSender stationMaxLandingPadSizeResponseSender(
            LoadStationPort loadStationPort,
            LoadStationMaxLandingPadSizeRequestByIdentifierPort loadStationMaxLandingPadSizeRequestByIdentifierPort,
            DeleteStationMaxLandingPadSizeRequestPort deleteStationMaxLandingPadSizeRequestPort,
            SendMessagePort sendMessagePort,
            ObjectMapper objectMapper,
            @Qualifier("explorationRetryTemplate") RetryTemplate retryTemplate,
            @Qualifier("virtualThreadPerTaskExecutor") ExecutorService executorService
    ) {
        return new StationMaxLandingPadSizeResponseSenderService(
                loadStationPort,
                loadStationMaxLandingPadSizeRequestByIdentifierPort,
                deleteStationMaxLandingPadSizeRequestPort,
                sendMessagePort,
                objectMapper,
                retryTemplate,
                executorService
        );
    }
}
