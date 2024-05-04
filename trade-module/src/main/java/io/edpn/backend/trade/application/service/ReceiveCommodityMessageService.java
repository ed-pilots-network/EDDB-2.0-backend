package io.edpn.backend.trade.application.service;

import io.edpn.backend.messageprocessorlib.application.dto.eddn.CommodityMessage;
import io.edpn.backend.trade.application.domain.Commodity;
import io.edpn.backend.trade.application.domain.MarketDatum;
import io.edpn.backend.trade.application.domain.Station;
import io.edpn.backend.trade.application.domain.System;
import io.edpn.backend.trade.application.port.incomming.kafka.ReceiveKafkaMessageUseCase;
import io.edpn.backend.trade.application.port.incomming.kafka.RequestDataUseCase;
import io.edpn.backend.trade.application.port.outgoing.commodity.CreateOrLoadCommodityPort;
import io.edpn.backend.trade.application.port.outgoing.marketdatum.CreateWhenNotExistsMarketDatumPort;
import io.edpn.backend.trade.application.port.outgoing.marketdatum.createOrUpdateExistingWhenNewerLatestMarketDatumPort;
import io.edpn.backend.trade.application.port.outgoing.station.CreateOrLoadStationPort;
import io.edpn.backend.trade.application.port.outgoing.station.UpdateStationPort;
import io.edpn.backend.trade.application.port.outgoing.system.CreateOrLoadSystemPort;
import io.edpn.backend.util.CollectionUtil;
import io.edpn.backend.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
public class ReceiveCommodityMessageService implements ReceiveKafkaMessageUseCase<CommodityMessage.V3> {

    private final IdGenerator idGenerator;
    private final CreateOrLoadSystemPort createOrLoadSystemPort;
    private final CreateOrLoadStationPort createOrLoadStationPort;
    private final CreateOrLoadCommodityPort createOrLoadCommodityPort;
    private final CreateWhenNotExistsMarketDatumPort createWhenNotExistsMarketDatumPort;
    private final createOrUpdateExistingWhenNewerLatestMarketDatumPort createOrUpdateOnConflictWhenNewerLatestMarketDatumPort;
    private final UpdateStationPort updateStationPort;
    private final List<RequestDataUseCase<Station>> stationRequestDataServices;
    private final List<RequestDataUseCase<System>> systemRequestDataServices;

    @Override
    @Transactional
    public void receive(CommodityMessage.V3 message) {

        long start = java.lang.System.nanoTime();
        if (log.isDebugEnabled()) {
            log.debug("DefaultReceiveCommodityMessageUseCase.receive -> CommodityMessage: " + message);
        }

        var updateTimestamp = message.messageTimeStamp();

        CommodityMessage.V3.Payload payload = message.message();
        CommodityMessage.V3.Commodity[] commodities = payload.commodities();
        long marketId = payload.marketId();
        var planetaryStationTypes = List.of("CraterOutpost", "CraterPort", "OnFootSettlement");
        boolean isPlanetary = planetaryStationTypes.contains(payload.stationType());
        boolean isFleetCarrier = payload.stationType().equals("FleetCarrier");
        boolean requiresOdyssey = payload.odyssey();
        String systemName = payload.systemName();
        String stationName = payload.stationName();
        String[] prohibitedCommodities = payload.prohibited();

        CompletableFuture<Station> stationCompletableFuture = CompletableFuture.supplyAsync(() -> createOrLoadSystemPort.createOrLoad(new System(
                        idGenerator.generateId(),
                        null,
                        systemName,
                        null)))
                .whenComplete((system, throwable) -> {
                    if (throwable != null) {
                        log.error("Exception occurred in retrieving system", throwable);
                    } else {
                        systemRequestDataServices.stream()
                                .filter(useCase -> useCase.isApplicable(system))
                                .forEach(useCase -> useCase.request(system));
                    }
                }).thenCompose(loadedSystem -> CompletableFuture.supplyAsync(() -> {
                    Station station = new Station(
                            idGenerator.generateId(),
                            marketId,
                            stationName,
                            null,
                            loadedSystem,
                            isPlanetary,
                            requiresOdyssey,
                            isFleetCarrier,
                            null,
                            updateTimestamp,
                            null
                    );
                    return createOrLoadStationPort.createOrLoad(station);
                }))
                .whenComplete((station, throwable) -> {
                    if (throwable != null) {
                        log.error("Exception occurred in retrieving station", throwable);
                    } else {
                        stationRequestDataServices.stream()
                                .filter(useCase -> useCase.isApplicable(station))
                                .forEach(useCase -> useCase.request(station));
                    }
                });

        // get marketDataCollection
        List<CompletableFuture<MarketDatum>> completableFutureList = Arrays.stream(commodities).parallel().map(commodityFromMessage -> {
                    // get commodity
                    return CompletableFuture.supplyAsync(() -> {
                        Commodity commodity1 = new Commodity(idGenerator.generateId(), commodityFromMessage.name());
                        return createOrLoadCommodityPort.createOrLoad(commodity1);
                    }).thenCompose(commodity -> CompletableFuture.supplyAsync(() -> getMarketDatum(commodity, commodityFromMessage, prohibitedCommodities, updateTimestamp)));
                })
                .toList();

        CompletableFuture<List<MarketDatum>> combinedFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]))
                .thenApply(v -> completableFutureList.stream()
                        .map(CompletableFuture::join)
                        .toList());

        stationCompletableFuture.thenCombine(combinedFuture, (station, marketDatumList) -> {
            marketDatumList.parallelStream().forEach(marketDatum -> {
                createWhenNotExistsMarketDatumPort.createWhenNotExists(station.id(), marketDatum);
                createOrUpdateOnConflictWhenNewerLatestMarketDatumPort.createOrUpdateWhenNewer(station.id(), marketDatum);
            });
            return station;
        });

        // save station changes
        updateStationPort.update(stationCompletableFuture.join());

        if (log.isTraceEnabled()) {
            log.trace("DefaultReceiveCommodityMessageUseCase.receive -> took " + (java.lang.System.nanoTime() - start) + " nanosecond");
        }

        log.debug("DefaultReceiveCommodityMessageUseCase.receive -> the message has been processed");
    }

    private MarketDatum getMarketDatum(Commodity commodity1, CommodityMessage.V3.Commodity commodity, String[] prohibitedCommodities, LocalDateTime timestamp) {
        return new MarketDatum(
                commodity1,
                timestamp,
                commodity.meanPrice(),
                commodity.buyPrice(),
                commodity.stock(),
                commodity.stockBracket(),
                commodity.sellPrice(),
                commodity.demand(),
                commodity.demandBracket(),
                CollectionUtil.toList(commodity.statusFlags()),
                Optional.ofNullable(prohibitedCommodities).map(prohibited -> Arrays.stream(prohibited).anyMatch(prohibitedCommodity -> prohibitedCommodity.equals(commodity.name()))).orElse(false)
        );
    }
}
