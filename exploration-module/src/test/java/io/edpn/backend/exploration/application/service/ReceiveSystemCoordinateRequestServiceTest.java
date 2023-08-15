package io.edpn.backend.exploration.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.backend.exploration.application.domain.Message;
import io.edpn.backend.exploration.application.domain.System;
import io.edpn.backend.exploration.application.domain.SystemCoordinateRequest;
import io.edpn.backend.exploration.application.dto.MessageDto;
import io.edpn.backend.exploration.application.dto.mapper.MessageMapper;
import io.edpn.backend.exploration.application.dto.mapper.SystemCoordinatesResponseMapper;
import io.edpn.backend.exploration.application.port.incomming.ReceiveKafkaMessageUseCase;
import io.edpn.backend.exploration.application.port.outgoing.message.SendMessagePort;
import io.edpn.backend.exploration.application.port.outgoing.system.LoadSystemPort;
import io.edpn.backend.exploration.application.port.outgoing.systemcoordinaterequest.CreateSystemCoordinateRequestPort;
import io.edpn.backend.exploration.application.port.outgoing.systemcoordinaterequest.LoadSystemCoordinateRequestPort;
import io.edpn.backend.messageprocessorlib.application.dto.eddn.data.SystemCoordinatesResponse;
import io.edpn.backend.messageprocessorlib.application.dto.eddn.data.SystemDataRequest;
import io.edpn.backend.util.Module;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReceiveSystemCoordinateRequestServiceTest {

    @Mock
    private CreateSystemCoordinateRequestPort createSystemCoordinateRequestPort;
    @Mock
    private LoadSystemCoordinateRequestPort loadSystemCoordinateRequestPort;
    @Mock
    private LoadSystemPort loadSystemPort;
    @Mock
    private SendMessagePort sendMessagePort;
    @Mock
    private SystemCoordinatesResponseMapper systemCoordinatesResponseMapper;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RetryTemplate retryTemplate;

    private ReceiveKafkaMessageUseCase<SystemDataRequest> underTest;

    @BeforeEach
    public void setup() {
        underTest = new ReceiveSystemCoordinateRequestService(createSystemCoordinateRequestPort, loadSystemCoordinateRequestPort, loadSystemPort, sendMessagePort, systemCoordinatesResponseMapper, messageMapper, objectMapper, retryTemplate);
    }

    @SneakyThrows
    @Test
    void testReceive_whenSystemExistsAndSendSuccessful_shouldNotSaveRequest() {

        SystemDataRequest message = mock(SystemDataRequest.class);
        String systemName = "system";
        System system = mock(System.class);
        Module requestingModule = mock(Module.class);
        when(requestingModule.getName()).thenReturn("module");
        when(message.systemName()).thenReturn(systemName);
        when(message.requestingModule()).thenReturn(requestingModule);
        when(loadSystemPort.load(systemName)).thenReturn(Optional.of(system));
        SystemCoordinatesResponse systemCoordinatesResponse = mock(SystemCoordinatesResponse.class);
        when(systemCoordinatesResponseMapper.map(system)).thenReturn(systemCoordinatesResponse);
        String jsonString = "{\"json\":\"response\"}";
        when(objectMapper.writeValueAsString(systemCoordinatesResponse)).thenReturn(jsonString);
        Message kafkaMessage = new Message("module_systemCoordinatesResponse", jsonString);
        MessageDto messageDto = mock(MessageDto.class);
        when(messageMapper.map(kafkaMessage)).thenReturn(messageDto);
        when(sendMessagePort.send(messageDto)).thenReturn(true);
        doAnswer(invocation -> ((RetryCallback<?, ?>) invocation.getArgument(0)).doWithRetry(null)).when(retryTemplate).execute(any());


        underTest.receive(message);


        verify(loadSystemPort).load(systemName);
        verify(sendMessagePort).send(messageDto);
        verify(createSystemCoordinateRequestPort, never()).create(any());
    }

    @SneakyThrows
    @Test
    void testReceive_whenSystemExistsAndSendFails_shouldSaveRequest() {

        SystemDataRequest message = mock(SystemDataRequest.class);
        String systemName = "system";
        Module requestingModule = mock(Module.class);
        when(requestingModule.getName()).thenReturn("module");
        System system = mock(System.class);
        when(message.systemName()).thenReturn(systemName);
        when(message.requestingModule()).thenReturn(requestingModule);
        when(loadSystemPort.load(systemName)).thenReturn(Optional.of(system));
        SystemCoordinatesResponse systemCoordinatesResponse = mock(SystemCoordinatesResponse.class);
        when(systemCoordinatesResponseMapper.map(system)).thenReturn(systemCoordinatesResponse);
        String jsonString = "{\"json\":\"response\"}";
        when(objectMapper.writeValueAsString(systemCoordinatesResponse)).thenReturn(jsonString);
        Message kafkaMessage = new Message("module_systemCoordinatesResponse", jsonString);
        MessageDto messageDto = mock(MessageDto.class);
        when(messageMapper.map(kafkaMessage)).thenReturn(messageDto);
        when(sendMessagePort.send(messageDto)).thenReturn(false);
        doAnswer(invocation -> ((RetryCallback<?, ?>) invocation.getArgument(0)).doWithRetry(null)).when(retryTemplate).execute(any());
        SystemCoordinateRequest systemCoordinateDataRequest = new SystemCoordinateRequest(systemName, requestingModule);


        underTest.receive(message);


        verify(loadSystemPort).load(systemName);
        verify(sendMessagePort).send(messageDto);
        verify(createSystemCoordinateRequestPort).create(systemCoordinateDataRequest);
    }

    @Test
    void testReceive_whenSystemDoesNotExist_shouldSaveRequest() {

        SystemDataRequest message = mock(SystemDataRequest.class);
        String systemName = "system";
        Module requestingModule = mock(Module.class);
        when(message.systemName()).thenReturn(systemName);
        when(message.requestingModule()).thenReturn(requestingModule);
        when(loadSystemPort.load(systemName)).thenReturn(Optional.empty());
        SystemCoordinateRequest systemCoordinateDataRequest = new SystemCoordinateRequest(systemName, requestingModule);


        underTest.receive(message);


        verify(loadSystemPort).load(systemName);
        verify(sendMessagePort, never()).send(any());
        verify(createSystemCoordinateRequestPort).create(systemCoordinateDataRequest);
    }

    @SneakyThrows
    @Test
    void testReceive_writeValueAsStringThrowsJsonProcessingException() {
        SystemDataRequest message = mock(SystemDataRequest.class);
        String systemName = "system";
        System system = mock(System.class);
        Module requestingModule = mock(Module.class);
        when(message.systemName()).thenReturn(systemName);
        when(message.requestingModule()).thenReturn(requestingModule);
        when(loadSystemPort.load(systemName)).thenReturn(Optional.of(system));
        SystemCoordinatesResponse systemCoordinatesResponse = mock(SystemCoordinatesResponse.class);
        when(systemCoordinatesResponseMapper.map(system)).thenReturn(systemCoordinatesResponse);
        when(objectMapper.writeValueAsString(systemCoordinatesResponse)).thenThrow(new JsonProcessingException("Test exception") {
        });

        assertThrows(RuntimeException.class, () -> underTest.receive(message));

        verify(loadSystemPort).load(systemName);
        verifyNoInteractions(sendMessagePort, createSystemCoordinateRequestPort);
    }

}
