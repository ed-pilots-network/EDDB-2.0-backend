package io.edpn.backend.trade.adapter.kafka.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.backend.messageprocessorlib.infrastructure.kafka.processor.MessageProcessor;
import io.edpn.backend.trade.application.domain.intermodulecommunication.StationArrivalDistanceResponse;
import io.edpn.backend.trade.application.port.incomming.kafka.ReceiveKafkaMessageUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StationArrivalDistanceResponseMessageProcessorTest {
    
    @Mock
    private ReceiveKafkaMessageUseCase<StationArrivalDistanceResponse> receiveKafkaMessageUseCase;
    
    @Mock
    private ObjectMapper objectMapper;
    
    private MessageProcessor<StationArrivalDistanceResponse> underTest;
    
    @BeforeEach
    void setup() {
        underTest = new StationArrivalDistanceResponseMessageProcessor(receiveKafkaMessageUseCase, objectMapper);
    }
    
    @Test
    void listen_shouldInvokeUseCaseWithCorrectStationArrivalDistanceResponse() throws JsonProcessingException {
        JsonNode jsonNode = mock(JsonNode.class);
        StationArrivalDistanceResponse stationArrivalDistanceResponse = new StationArrivalDistanceResponse(null, null, 20);
        
        Mockito.when(objectMapper.treeToValue(jsonNode, StationArrivalDistanceResponse.class)).thenReturn(stationArrivalDistanceResponse);
        
        underTest.listen(jsonNode);
        
        verify(receiveKafkaMessageUseCase, times(1)).receive(any(StationArrivalDistanceResponse.class));
        
        
    }
    
}