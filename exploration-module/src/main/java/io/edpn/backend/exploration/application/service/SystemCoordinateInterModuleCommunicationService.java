package io.edpn.backend.exploration.application.service;

import io.edpn.backend.exploration.application.domain.SystemCoordinateRequest;
import io.edpn.backend.exploration.application.domain.SystemCoordinatesUpdatedEvent;
import io.edpn.backend.exploration.application.port.incomming.ProcessPendingDataRequestUseCase;
import io.edpn.backend.exploration.application.port.incomming.ReceiveKafkaMessageUseCase;
import io.edpn.backend.exploration.application.port.outgoing.system.LoadSystemPort;
import io.edpn.backend.exploration.application.port.outgoing.systemcoordinaterequest.CreateIfNotExistsSystemCoordinateRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.systemcoordinaterequest.LoadAllSystemCoordinateRequestPort;
import io.edpn.backend.messageprocessorlib.application.dto.eddn.data.SystemDataRequest;
import io.edpn.backend.util.Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
@Slf4j
public class SystemCoordinateInterModuleCommunicationService implements ReceiveKafkaMessageUseCase<SystemDataRequest>, ProcessPendingDataRequestUseCase<SystemCoordinateRequest> {


    private final LoadAllSystemCoordinateRequestPort loadAllSystemCoordinateRequestPort;
    private final CreateIfNotExistsSystemCoordinateRequestPort createIfNotExistsSystemCoordinateRequestPort;
    private final LoadSystemPort loadSystemPort;
    private final ApplicationEventPublisher eventPublisher;
    private final ExecutorService executorService;


    @Override
    public void receive(SystemDataRequest message) {
        String systemName = message.systemName();
        Module requestingModule = message.requestingModule();

        saveRequest(systemName, requestingModule);
        executorService.submit(sendEventIfDataExists(systemName));
    }

    @Override
    @Scheduled(cron = "0 0 0/12 * * *")
    public void processPending() {
        loadAllSystemCoordinateRequestPort.loadAll()
                .parallelStream()
                .map(SystemCoordinateRequest::systemName)
                .map(this::sendEventIfDataExists)
                .forEach(executorService::submit);
    }

    private void saveRequest(String systemName, Module requestingModule) {
        SystemCoordinateRequest systemCoordinateDataRequest = new SystemCoordinateRequest(systemName, requestingModule);
        createIfNotExistsSystemCoordinateRequestPort.createIfNotExists(systemCoordinateDataRequest);
    }

    private Runnable sendEventIfDataExists(String systemName) {
        return () -> loadSystemPort.load(systemName)
                .ifPresent(system -> eventPublisher.publishEvent(new SystemCoordinatesUpdatedEvent(this, system.name())));
    }
}
