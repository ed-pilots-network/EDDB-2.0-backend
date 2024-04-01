package io.edpn.backend.exploration.application.service;

import io.edpn.backend.exploration.application.domain.Coordinate;
import io.edpn.backend.exploration.application.domain.LandingPadSize;
import io.edpn.backend.exploration.application.domain.Station;
import io.edpn.backend.exploration.application.domain.System;
import io.edpn.backend.exploration.application.port.incomming.ReceiveKafkaMessageUseCase;
import io.edpn.backend.exploration.application.port.outgoing.station.SaveOrUpdateStationPort;
import io.edpn.backend.exploration.application.port.outgoing.stationmaxlandingpadsizerequest.StationMaxLandingPadSizeResponseSender;
import io.edpn.backend.exploration.application.port.outgoing.system.SaveOrUpdateSystemPort;
import io.edpn.backend.exploration.application.port.outgoing.systemcoordinaterequest.SystemCoordinatesResponseSender;
import io.edpn.backend.exploration.application.port.outgoing.systemeliteidrequest.SystemEliteIdResponseSender;
import io.edpn.backend.messageprocessorlib.application.dto.eddn.journal.DockedMessage;
import io.edpn.backend.util.ConcurrencyUtil;
import io.edpn.backend.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ReceiveJournalDockedService implements ReceiveKafkaMessageUseCase<DockedMessage.V1> {

    private final IdGenerator idGenerator;
    private final SystemCoordinatesResponseSender systemCoordinatesResponseSender;
    private final SystemEliteIdResponseSender systemEliteIdResponseSender;
    private final StationMaxLandingPadSizeResponseSender stationMaxLandingPadSizeResponseSender;
    private final SaveOrUpdateStationPort saveOrUpdateStationPort;
    private final SaveOrUpdateSystemPort saveOrUpdateSystemPort;
    private final ExecutorService executorService;

    @Transactional
    @Override
    public void receive(DockedMessage.V1 message) {
        long start = java.lang.System.nanoTime();
        log.debug("DefaultReceiveJournalDockedMessageMessageUseCase.receive -> BodyMessage: {}", message);

        DockedMessage.V1.Payload payload = message.message();
        LocalDateTime messageTimeStamp = message.messageTimeStamp();

        createOrUpdateFromPayload(payload, messageTimeStamp);

        log.trace("DefaultReceiveJournalDockedMessageMessageUseCase.receive -> took {} nanosecond", java.lang.System.nanoTime() - start);
        log.debug("DefaultReceiveJournalDockedMessageMessageUseCase.receive -> the message has been processed");
    }


    private void createOrUpdateFromPayload(DockedMessage.V1.Payload payload, LocalDateTime messageTimeStamp) {
        Station station = saveOrUpdateStationFromPayload(payload, messageTimeStamp);

        executorService.submit(ConcurrencyUtil.errorHandlingWrapper(
                () -> systemCoordinatesResponseSender.sendResponsesForSystem(station.system().name()),
                exception -> log.error("Error while sending systemCoordinatesResponse for system: {}", station.system().name(), exception)));
        executorService.submit(ConcurrencyUtil.errorHandlingWrapper(
                () -> systemEliteIdResponseSender.sendResponsesForSystem(station.system().name()),
                exception -> log.error("Error while sending systemEliteIdResponse for system: {}", station.system().name(), exception)));
        executorService.submit(ConcurrencyUtil.errorHandlingWrapper(
                () -> stationMaxLandingPadSizeResponseSender.sendResponsesForStation(station.system().name(), station.name()),
                exception -> log.error("Error while sending stationMaxLandingPadSizeResponse for station: {}", station.name(), exception)));
    }

    private Map<LandingPadSize, Integer> landingPadSizeFromPayload(DockedMessage.V1.Payload payload) {
        return Optional.ofNullable(payload.landingPads())
                .map(landingPads -> Map.of(
                        LandingPadSize.LARGE, landingPads.large(),
                        LandingPadSize.MEDIUM, landingPads.medium(),
                        LandingPadSize.SMALL, landingPads.small()
                )).orElse(null);
    }

    private Map<String, Double> stationEconomiesFromPayload(DockedMessage.V1.Payload payload) {
        return payload.stationEconomies().stream().
                collect(Collectors.toMap(
                        DockedMessage.V1.Payload.Economy::name,
                        DockedMessage.V1.Payload.Economy::proportion,
                        (first, second) -> first
                ));
    }

    private Station saveOrUpdateStationFromPayload(DockedMessage.V1.Payload payload, LocalDateTime messageTimeStamp) {
        return saveOrUpdateStationPort.saveOrUpdate(
                new Station(
                        idGenerator.generateId(),
                        payload.marketId(),
                        payload.stationName(),
                        payload.stationType(),
                        payload.distanceFromStar(),
                        saveOrUpdateSystemFromPayload(payload),
                        landingPadSizeFromPayload(payload),
                        stationEconomiesFromPayload(payload),
                        payload.stationServices(),
                        payload.stationEconomy(),
                        payload.stationGovernment(),
                        payload.odyssey(),
                        payload.horizons(),
                        messageTimeStamp));
    }

    private System saveOrUpdateSystemFromPayload(DockedMessage.V1.Payload payload) {
        return saveOrUpdateSystemPort.saveOrUpdate(
                new System(
                        idGenerator.generateId(),
                        payload.systemAddress(),
                        payload.starSystem(),
                        null,
                        starPositionToCoordinate(payload.starPosition())));
    }

    private Coordinate starPositionToCoordinate(Double[] starPosition) {
        if (starPosition == null || starPosition.length != 3) {
            throw new IllegalArgumentException("starPosition must be an array of 3 doubles");
        }
        return new Coordinate(starPosition[0], starPosition[1], starPosition[2]);
    }
}
