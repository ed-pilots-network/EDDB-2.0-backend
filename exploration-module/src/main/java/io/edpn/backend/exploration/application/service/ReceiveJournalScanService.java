package io.edpn.backend.exploration.application.service;

import io.edpn.backend.exploration.application.domain.Body;
import io.edpn.backend.exploration.application.domain.Ring;
import io.edpn.backend.exploration.application.domain.Star;
import io.edpn.backend.exploration.application.domain.System;
import io.edpn.backend.exploration.application.port.incomming.ReceiveKafkaMessageUseCase;
import io.edpn.backend.exploration.application.port.outgoing.body.SaveOrUpdateBodyPort;
import io.edpn.backend.exploration.application.port.outgoing.ring.SaveOrUpdateRingPort;
import io.edpn.backend.exploration.application.port.outgoing.star.SaveOrUpdateStarPort;
import io.edpn.backend.exploration.application.port.outgoing.system.SaveOrUpdateSystemPort;
import io.edpn.backend.messageprocessorlib.application.dto.eddn.journal.ScanMessage;
import io.edpn.backend.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ReceiveJournalScanService implements ReceiveKafkaMessageUseCase<ScanMessage.V1> {

    private final IdGenerator idGenerator;
    private final SaveOrUpdateBodyPort saveOrUpdateBodyPort;
    private final SaveOrUpdateRingPort saveOrUpdateRingPort;
    private final SaveOrUpdateStarPort saveOrUpdateStarPort;
    private final SaveOrUpdateSystemPort saveOrUpdateSystemPort;

    @Transactional
    @Override
    public void receive(ScanMessage.V1 message) {
        long start = java.lang.System.nanoTime();
        log.debug("DefaultReceiveJournalScanMessageUseCase.receive -> BodyMessage: {}", message);

        ScanMessage.V1.Payload payload = message.message();

        createOrUpdateFromPayload(payload);

        log.trace("DefaultReceiveJournalScanMessageUseCase.receive -> took {} nanosecond", java.lang.System.nanoTime() - start);
        log.debug("DefaultReceiveJournalScanMessageUseCase.receive -> the message has been processed");
    }


    private void createOrUpdateFromPayload(ScanMessage.V1.Payload payload) {

        if (isStarPayload(payload)) {
            UUID starId = idGenerator.generateId();

            Star payloadStar = new Star(
                    starId,
                    payload.absoluteMagnitude(),
                    payload.age(),
                    payload.arrivalDistance(),
                    payload.axialTilt(),
                    payload.discovered(),
                    payload.localID(),
                    payload.luminosity(),
                    payload.mapped(),
                    payload.name(),
                    payload.radius(),
                    Optional.ofNullable(payload.rings())
                            .orElse(new ArrayList<>())
                            .stream()
                            .map(messageRing ->
                                    new Ring(
                                            idGenerator.generateId(),
                                            messageRing.innerRadius(),
                                            messageRing.mass(),
                                            messageRing.name(),
                                            messageRing.outerRadius(),
                                            messageRing.ringClass(),
                                            null,
                                            starId))
                            .toList(),
                    payload.rotationPeriod(),
                    payload.starType(),
                    payload.stellarMass(),
                    payload.subclass(),
                    payload.surfaceTemperature(),
                    saveOrUpdateSystemPort.saveOrUpdate(
                            System.builder()
                                    .id(idGenerator.generateId())
                                    .name(payload.system())
                                    .build()
                    ),
                    payload.systemAddress(),
                    payload.horizons(),
                    payload.odyssey(),
                    null);

            for (Ring ring : payloadStar.rings()) {
                saveOrUpdateRingPort.saveOrUpdate(ring);
            }

            saveOrUpdateStarPort.saveOrUpdate(payloadStar);
        } else if (isBodyPayload(payload)) {
            UUID bodyId = idGenerator.generateId();

            Body payloadBody = new Body(
                    bodyId,
                    payload.arrivalDistance(),
                    payload.ascendingNode(),
                    payload.atmosphere(),
                    Optional.ofNullable(payload.atmosphereComposition())
                            .orElse(new ArrayList<>())
                            .stream()
                            .collect(Collectors.toMap(
                                    ScanMessage.V1.Payload.AtmosphericPercentage::name,
                                    ScanMessage.V1.Payload.AtmosphericPercentage::percent,
                                    (first, second) -> first
                            )),
                    payload.axialTilt(),
                    payload.bodyComposition(),
                    payload.discovered(),
                    payload.mapped(),
                    payload.name(),
                    payload.localID(),
                    payload.eccentricity(),
                    payload.landable(),
                    payload.mass(),
                    Optional.ofNullable(payload.materials())
                            .orElse(new ArrayList<>())
                            .stream()
                            .collect(Collectors.toMap(
                                    ScanMessage.V1.Payload.MaterialsPercentage::name,
                                    ScanMessage.V1.Payload.MaterialsPercentage::percent,
                                    (first, second) -> first
                            )),
                    payload.meanAnomaly(),
                    payload.orbitalInclination(),
                    payload.orbitalPeriod(),
                    Optional.ofNullable(payload.parents())
                            .orElse(new ArrayList<>())
                            .stream()
                            .flatMap(map -> map.entrySet().stream())
                            .collect(Collectors.toMap(
                                    Map.Entry::getValue,
                                    Map.Entry::getKey,
                                    (first, second) -> first
                            )),
                    payload.periapsis(),
                    payload.planetClass(),
                    payload.radius(),
                    Optional.ofNullable(payload.rings())
                            .orElse(new ArrayList<>())
                            .stream()
                            .map(messageRing ->
                                    new Ring(
                                            idGenerator.generateId(),
                                            messageRing.innerRadius(),
                                            messageRing.mass(),
                                            messageRing.name(),
                                            messageRing.outerRadius(),
                                            messageRing.ringClass(),
                                            bodyId,
                                            null))
                            .toList(),
                    payload.rotationPeriod(),
                    payload.semiMajorAxis(),
                    payload.surfaceGravity(),
                    payload.surfacePressure(),
                    payload.surfaceTemperature(),
                    saveOrUpdateSystemPort.saveOrUpdate(
                            System.builder()
                                    .id(idGenerator.generateId())
                                    .name(payload.system())
                                    .build()
                    ),
                    payload.systemAddress(),
                    payload.terraformState(),
                    payload.tidalLock(),
                    payload.volcanism(),
                    payload.horizons(),
                    payload.odyssey(),
                    null
            );

            for (Ring ring : payloadBody.rings()) {
                saveOrUpdateRingPort.saveOrUpdate(ring);
            }

            saveOrUpdateBodyPort.saveOrUpdate(payloadBody);
        }
    }

    private boolean isStarPayload(ScanMessage.V1.Payload payload) {
        return payload.stellarMass() != null;
    }

    private boolean isBodyPayload(ScanMessage.V1.Payload payload) {
        return payload.planetClass() != null;
    }

}
