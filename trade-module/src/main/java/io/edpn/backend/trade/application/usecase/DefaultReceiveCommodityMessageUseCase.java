package io.edpn.backend.trade.application.usecase;

import io.edpn.backend.trade.domain.model.Commodity;
import io.edpn.backend.trade.domain.model.MarketDatum;
import io.edpn.backend.trade.domain.model.Station;
import io.edpn.backend.trade.domain.model.System;
import io.edpn.backend.trade.domain.repository.CommodityRepository;
import io.edpn.backend.trade.domain.repository.MarketDatumRepository;
import io.edpn.backend.trade.domain.repository.StationRepository;
import io.edpn.backend.trade.domain.repository.SystemRepository;
import io.edpn.backend.trade.domain.usecase.ReceiveCommodityMessageUseCase;
import io.edpn.backend.trade.domain.service.RequestDataService;
import io.edpn.backend.messageprocessorlib.application.dto.eddn.CommodityMessage;
import io.edpn.backend.util.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class DefaultReceiveCommodityMessageUseCase implements ReceiveCommodityMessageUseCase {

    private final CommodityRepository commodityRepository;
    private final SystemRepository systemRepository;
    private final StationRepository stationRepository;
    private final MarketDatumRepository marketDatumRepository;
    private final List<RequestDataService<Station>> stationRequestDataServices;
    private final List<RequestDataService<System>> systemRequestDataServices;

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
        String systemName = payload.systemName();
        String stationName = payload.stationName();
        String[] prohibitedCommodities = payload.prohibited();

        //do we have this data already?
        if (marketDatumRepository.existsByStationNameAndSystemNameAndTimestamp(systemName, stationName, updateTimestamp)) {
            log.debug("data with the same key as received message already present in database. SKIPPING");
            return;
        }


        // get system
        CompletableFuture<System> systemCompletableFuture = CompletableFuture.supplyAsync(() -> systemRepository.findOrCreateByName(systemName))
                .whenComplete((system, throwable) -> {
                    if (throwable != null) {
                        log.error("Exception occurred in retrieving system", throwable);
                    } else {
                        systemRequestDataServices.stream()
                                .filter(useCase -> useCase.isApplicable(system))
                                .forEach(useCase -> useCase.request(system));
                    }
                });

        // get station
        CompletableFuture<Station> stationCompletableFuture = CompletableFuture.supplyAsync(() -> stationRepository.findOrCreateBySystemAndStationName(systemCompletableFuture.copy().join(), stationName));
        stationCompletableFuture.whenComplete((station, throwable) -> {
            if (throwable != null) {
                log.error("Exception occurred in retrieving station", throwable);
            } else {
                //if we get the message here, it always is NOT a fleet carrier
                station.setFleetCarrier(false);

                if (Objects.isNull(station.getMarketId())) {
                    station.setMarketId(marketId);
                }

                if (Objects.isNull(station.getMarketUpdatedAt()) || updateTimestamp.isAfter(station.getMarketUpdatedAt())) {
                    station.setMarketUpdatedAt(updateTimestamp);
                }

                stationRequestDataServices.stream()
                        .filter(useCase -> useCase.isApplicable(station))
                        .forEach(useCase -> useCase.request(station));
            }
        });

        // get marketDataCollection
        List<CompletableFuture<MarketDatum>> completableFutureList = Arrays.stream(commodities).parallel().map(commodityFromMessage -> {
                    // get commodity
                    CompletableFuture<Commodity> commodity = CompletableFuture.supplyAsync(() -> commodityRepository.findOrCreateByName(commodityFromMessage.name()));
                    // parse market data
                    CompletableFuture<MarketDatum> marketDatum = CompletableFuture.supplyAsync(() -> getMarketDatum(commodityFromMessage, prohibitedCommodities, updateTimestamp));

                    return commodity.thenCombine(marketDatum, (c, md) -> {
                        md.setCommodity(c);
                        return md;
                    });
                })
                .toList();

        CompletableFuture<List<MarketDatum>> combinedFuture = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]))
                .thenApply(v -> completableFutureList.stream()
                        .map(CompletableFuture::join)
                        .toList());


        // put market data map in station
        stationCompletableFuture.thenCombine(combinedFuture, (station, marketDataMap) -> {
            station.setMarketData(marketDataMap);
            return station;
        });

        // save station
        stationRepository.update(stationCompletableFuture.join());

        if (log.isTraceEnabled()) {
            log.trace("DefaultReceiveCommodityMessageUseCase.receive -> took " + (java.lang.System.nanoTime() - start) + " nanosecond");
        }

        log.info("DefaultReceiveCommodityMessageUseCase.receive -> the message has been processed");
    }

    private MarketDatum getMarketDatum(CommodityMessage.V3.Commodity commodity, String[] prohibitedCommodities, LocalDateTime timestamp) {
        return MarketDatum.builder()
                .timestamp(timestamp)
                .meanPrice(commodity.meanPrice())
                .buyPrice(commodity.buyPrice())
                .sellPrice(commodity.sellPrice())
                .stock(commodity.stock())
                .stockBracket(commodity.stockBracket())
                .demand(commodity.demand())
                .demandBracket(commodity.demandBracket())
                .statusFlags(CollectionUtil.toList(commodity.statusFlags()))
                .prohibited(Arrays.stream(prohibitedCommodities).anyMatch(prohibitedCommodity -> prohibitedCommodity.equals(commodity.name())))
                .build();
    }
}
