package io.edpn.backend.trade.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.backend.trade.application.domain.Coordinate;
import io.edpn.backend.trade.application.domain.Message;
import io.edpn.backend.trade.application.domain.System;
import io.edpn.backend.trade.application.dto.web.object.mapper.MessageMapper;
import io.edpn.backend.trade.application.port.incomming.kafka.RequestDataUseCase;
import io.edpn.backend.trade.application.port.outgoing.kafka.SendKafkaMessagePort;
import io.edpn.backend.trade.application.port.outgoing.system.CreateOrLoadSystemPort;
import io.edpn.backend.trade.application.port.outgoing.system.LoadSystemsByFilterPort;
import io.edpn.backend.trade.application.port.outgoing.system.UpdateSystemPort;
import io.edpn.backend.trade.application.port.outgoing.systemcoordinaterequest.CreateSystemCoordinateRequestPort;
import io.edpn.backend.trade.application.port.outgoing.systemcoordinaterequest.DeleteSystemCoordinateRequestPort;
import io.edpn.backend.trade.application.port.outgoing.systemcoordinaterequest.ExistsSystemCoordinateRequestPort;
import io.edpn.backend.trade.application.port.outgoing.systemcoordinaterequest.LoadAllSystemCoordinateRequestsPort;
import io.edpn.backend.util.IdGenerator;
import io.edpn.backend.util.Topic;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.support.RetryTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RequestSystemCoordinatesServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private IdGenerator idGenerator;
    @Mock
    private LoadSystemsByFilterPort loadSystemsByFilterPort;
    @Mock
    private LoadAllSystemCoordinateRequestsPort loadAllSystemCoordinateRequestsPort;
    @Mock
    private CreateOrLoadSystemPort createOrLoadSystemPort;
    @Mock
    private ExistsSystemCoordinateRequestPort existsSystemCoordinateRequestPort;
    @Mock
    private CreateSystemCoordinateRequestPort createSystemCoordinateRequestPort;
    @Mock
    private DeleteSystemCoordinateRequestPort deleteSystemCoordinateRequestPort;
    @Mock
    private UpdateSystemPort updateSystemPort;
    @Mock
    private SendKafkaMessagePort sendKafkaMessagePort;
    @Mock
    private RetryTemplate retryTemplate;
    @Mock
    private Executor executor;
    @Mock
    private MessageMapper messageMapper;
    private RequestDataUseCase<System> underTest;

    public static Stream<Arguments> provideDoublesForCheckApplicability() {
        return Stream.of(
                Arguments.of(null, null, null, true),
                Arguments.of(0.0, null, null, true),
                Arguments.of(null, 0.0, null, true),
                Arguments.of(null, null, 0.0, true),
                Arguments.of(0.0, 0.0, 0.0, false),
                Arguments.of(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, false)
        );
    }

    @BeforeEach
    void setUp() {
        underTest = new SystemCoordinateInterModuleCommunicationService(
                idGenerator,
                loadSystemsByFilterPort,
                loadAllSystemCoordinateRequestsPort,
                createOrLoadSystemPort,
                existsSystemCoordinateRequestPort,
                createSystemCoordinateRequestPort,
                deleteSystemCoordinateRequestPort,
                updateSystemPort,
                sendKafkaMessagePort,
                retryTemplate,
                executor,
                objectMapper,
                messageMapper
        );
    }

    @ParameterizedTest
    @MethodSource("provideDoublesForCheckApplicability")
    void shouldCheckApplicability(Double xCoordinate, Double yCoordinate, Double zCoordinate, boolean expected) {
        System systemWithCoordinates = new System(
                null,
                null,
                null,
                new Coordinate(xCoordinate, yCoordinate, zCoordinate)
        );

        assertThat(underTest.isApplicable(systemWithCoordinates), is(expected));
    }

    @Test
    void shouldSendRequest() {
        System system = new System(
                null,
                null,
                "Test System",
                null
        );

        underTest.request(system);

        ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messageMapper, times(1)).map(argumentCaptor.capture());
        verify(sendKafkaMessagePort, times(1)).send(messageMapper.map(argumentCaptor.capture()));

        Message message = argumentCaptor.getValue();
        assertThat(message, is(notNullValue()));
        assertThat(message.topic(), is(Topic.Request.SYSTEM_COORDINATES.getTopicName()));
        assertThat(message.message(), is(notNullValue()));

        //TODO: below
        //SystemDataRequest actualSystemDataRequest = objectMapper.treeToValue(message.getMessage(), SystemDataRequest.class);
        assertThat(message.message(), containsString(system.name()));
    }
}
