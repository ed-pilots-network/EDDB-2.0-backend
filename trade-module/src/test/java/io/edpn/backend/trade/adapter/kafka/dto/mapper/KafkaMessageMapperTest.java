package io.edpn.backend.trade.adapter.kafka.dto.mapper;

import io.edpn.backend.trade.application.domain.Message;
import io.edpn.backend.trade.application.dto.web.object.MessageDto;
import io.edpn.backend.trade.application.dto.web.object.mapper.MessageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class KafkaMessageMapperTest {
    
    private MessageMapper underTest;
    
    @BeforeEach
    void setUp() {
        underTest = new KafkaMessageMapper();
    }
    
    @Test
    void map_shouldReturnCorrectKafkaMessageDto() {
        
        String topic = "test-topic";
        String message = "test-message";
        Message kafkaMessage = new Message(topic, message);
        
        
        MessageDto result = underTest.map(kafkaMessage);
        
        
        assertThat(result.topic(), is(topic));
        assertThat(result.message(), is(message));
    }
    
}