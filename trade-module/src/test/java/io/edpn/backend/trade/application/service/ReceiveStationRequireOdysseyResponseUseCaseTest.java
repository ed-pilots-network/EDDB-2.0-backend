package io.edpn.backend.trade.application.service;

import io.edpn.backend.messageprocessorlib.application.dto.eddn.data.StationRequireOdysseyResponse;
import io.edpn.backend.trade.application.domain.Station;
import io.edpn.backend.trade.application.domain.System;
import io.edpn.backend.trade.application.port.incomming.kafka.ReceiveKafkaMessageUseCase;
import io.edpn.backend.trade.application.port.outgoing.station.LoadOrCreateBySystemAndStationNamePort;
import io.edpn.backend.trade.application.port.outgoing.station.UpdateStationPort;
import io.edpn.backend.trade.application.port.outgoing.stationrequireodysseyrequest.DeleteStationRequireOdysseyRequestPort;
import io.edpn.backend.trade.application.port.outgoing.system.LoadOrCreateSystemByNamePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReceiveStationRequireOdysseyResponseUseCaseTest {

    @Mock
    private LoadOrCreateSystemByNamePort loadOrCreateSystemByNamePort;

    @Mock
    private LoadOrCreateBySystemAndStationNamePort loadOrCreateBySystemAndStationNamePort;

    @Mock
    private DeleteStationRequireOdysseyRequestPort deleteStationRequireOdysseyRequestPort;

    @Mock
    private UpdateStationPort updateStationPort;

    private ReceiveKafkaMessageUseCase<StationRequireOdysseyResponse> underTest;

    @BeforeEach
    public void setUp() {
        underTest = new ReceiveStationRequireOdysseyResponseService(loadOrCreateSystemByNamePort, loadOrCreateBySystemAndStationNamePort, deleteStationRequireOdysseyRequestPort, updateStationPort);
    }

    @Test
    public void shouldReceiveStationRequireOdysseyResponse() {
        StationRequireOdysseyResponse message =
                new StationRequireOdysseyResponse("station", "system", true);

        System system = mock(System.class);
        when(loadOrCreateSystemByNamePort.loadOrCreateSystemByName("system")).thenReturn(system);

        Station station = Station.builder()
                .name("station")
                .build();
        when(loadOrCreateBySystemAndStationNamePort.loadOrCreateBySystemAndStationName(system, "station")).thenReturn(station);

        underTest.receive(message);

        verify(loadOrCreateSystemByNamePort, times(1)).loadOrCreateSystemByName(anyString());
        verify(loadOrCreateBySystemAndStationNamePort, times(1)).loadOrCreateBySystemAndStationName(any(), anyString());
        verify(updateStationPort, times(1)).update(any());
        verify(deleteStationRequireOdysseyRequestPort, times(1)).delete("system", "station");

        assert (station.getRequireOdyssey());
    }
}
