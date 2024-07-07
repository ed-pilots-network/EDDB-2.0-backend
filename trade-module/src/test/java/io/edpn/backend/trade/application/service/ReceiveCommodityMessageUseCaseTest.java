package io.edpn.backend.trade.application.service;

import io.edpn.backend.messageprocessorlib.application.dto.eddn.CommodityMessage;
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
import io.edpn.backend.util.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiveCommodityMessageUseCaseTest {

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private CreateOrLoadSystemPort createOrLoadSystemPort;

    @Mock
    private CreateOrLoadStationPort createOrLoadStationPort;

    @Mock
    private CreateOrLoadCommodityPort createOrLoadCommodityPort;

    @Mock
    private UpdateStationPort updateStationPort;

    @Mock
    private CreateWhenNotExistsMarketDatumPort createWhenNotExistsMarketDatumPort;

    @Mock
    private createOrUpdateExistingWhenNewerLatestMarketDatumPort createOrUpdateOnConflictWhenNewerLatestMarketDatumPort;

    @Mock
    private List<RequestDataUseCase<Station>> stationRequestDataServices;

    @Mock
    private List<RequestDataUseCase<System>> systemRequestDataServices;

    private ReceiveKafkaMessageUseCase<CommodityMessage.V3> underTest;

    @BeforeEach
    public void setUp() {
        underTest = new ReceiveCommodityMessageService(
                idGenerator,
                createOrLoadSystemPort,
                createOrLoadStationPort,
                createOrLoadCommodityPort,
                createWhenNotExistsMarketDatumPort,
                createOrUpdateOnConflictWhenNewerLatestMarketDatumPort,
                updateStationPort,
                stationRequestDataServices,
                systemRequestDataServices);
    }

    @Test
    public void shouldReceiveMessage() {

        CommodityMessage.V3 message = mock(CommodityMessage.V3.class);

        LocalDateTime timestamp = LocalDateTime.now();

        CommodityMessage.V3.Commodity mockCommodity = mock(CommodityMessage.V3.Commodity.class);

        CommodityMessage.V3.Commodity[] list = new CommodityMessage.V3.Commodity[1];
        list[0] = mockCommodity;

        CommodityMessage.V3.Payload payload =
                new CommodityMessage.V3.Payload(
                        "system",
                        "station",
                        null,
                        null,
                        123456L,
                        true,
                        true,
                        timestamp.toString(),
                        null,
                        null,
                        list);

        when(message.message()).thenReturn(payload);
        when(message.messageTimeStamp()).thenReturn(timestamp);

        System system = mock(System.class);
        when(createOrLoadSystemPort.createOrLoad(argThat(argument -> argument.name().equals("system")))).thenReturn(system);

        Station station = mock(Station.class);
        when(createOrLoadStationPort.createOrLoad(argThat(argument -> argument.system().equals(system) && argument.name().equals("station")))).thenReturn(station);

        underTest.receive(message);

        verify(updateStationPort, times(1)).update(station);
    }
}