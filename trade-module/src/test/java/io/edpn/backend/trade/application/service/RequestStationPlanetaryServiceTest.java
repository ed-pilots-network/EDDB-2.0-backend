package io.edpn.backend.trade.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.backend.messageprocessorlib.application.dto.eddn.data.StationDataRequest;
import io.edpn.backend.trade.application.domain.Message;
import io.edpn.backend.trade.application.domain.Station;
import io.edpn.backend.trade.application.domain.System;
import io.edpn.backend.trade.application.dto.web.object.MessageDto;
import io.edpn.backend.trade.application.dto.web.object.mapper.MessageMapper;
import io.edpn.backend.trade.application.port.incomming.kafka.RequestDataUseCase;
import io.edpn.backend.trade.application.port.outgoing.kafka.SendKafkaMessagePort;
import io.edpn.backend.trade.application.port.outgoing.stationplanetaryrequest.CreateStationPlanetaryRequestPort;
import io.edpn.backend.trade.application.port.outgoing.stationplanetaryrequest.ExistsStationPlanetaryRequestPort;
import io.edpn.backend.util.Module;
import io.edpn.backend.util.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestStationPlanetaryServiceTest {

    @Mock
    private SendKafkaMessagePort sendKafkaMessagePort;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ExistsStationPlanetaryRequestPort existsStationPlanetaryRequestPort;
    @Mock
    private CreateStationPlanetaryRequestPort createStationPlanetaryRequestPort;

    @Mock
    private MessageMapper messageMapper;

    private RequestDataUseCase<Station> underTest;


    public static Stream<Arguments> provideBooleansForCheckApplicability() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of(Boolean.TRUE, false),
                Arguments.of(Boolean.FALSE, false)
        );
    }

    @BeforeEach
    void setUp() {
        underTest = new RequestStationPlanetaryService(sendKafkaMessagePort, existsStationPlanetaryRequestPort, createStationPlanetaryRequestPort, objectMapper, messageMapper);
    }

    @ParameterizedTest
    @MethodSource("provideBooleansForCheckApplicability")
    void shouldCheckApplicability(Boolean input, boolean expected) {
        Station stationWithPlanetary = mock(Station.class);
        when(stationWithPlanetary.getPlanetary()).thenReturn(input);

        assertThat(underTest.isApplicable(stationWithPlanetary), is(expected));
    }

    @Test
    public void testRequestWhenIdDoesNotExist() {
        String systemName = "Test System";
        String stationName = "Test Station";

        System system = System.builder()
                .name(systemName)
                .build();
        Station station = Station.builder()
                .name(stationName)
                .system(system)
                .build();

        JsonNode mockJsonNode = mock(JsonNode.class);
        String mockJsonString = "jsonString";
        MessageDto mockMessageDto = mock(MessageDto.class);

        when(existsStationPlanetaryRequestPort.exists(systemName, stationName)).thenReturn(false);
        when(objectMapper.valueToTree(argThat(arg -> {
            if (arg instanceof StationDataRequest stationDataRequest) {
                return systemName.equals(stationDataRequest.systemName()) && stationName.equals(stationDataRequest.stationName()) && Module.TRADE.equals(stationDataRequest.requestingModule());
            } else {
                return false;
            }
        }))).thenReturn(mockJsonNode);
        when(mockJsonNode.toString()).thenReturn(mockJsonString);
        when(messageMapper.map(argThat(argument ->
                argument.getMessage().equals(mockJsonString) && argument.getTopic().equals(Topic.Request.STATION_IS_PLANETARY.getTopicName())
        ))).thenReturn(mockMessageDto);

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);


        underTest.request(station);

        verify(sendKafkaMessagePort).send(mockMessageDto);
        verify(createStationPlanetaryRequestPort).create(systemName, stationName);
        verify(messageMapper, times(1)).map(argumentCaptor.capture());
        Message message = argumentCaptor.getValue();
        assertThat(message, is(notNullValue()));
        assertThat(message.getTopic(), is(Topic.Request.STATION_IS_PLANETARY.getTopicName()));
        assertThat(message.getMessage(), is("jsonString"));
    }
}
