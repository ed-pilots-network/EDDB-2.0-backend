package io.edpn.backend.trade.adapter.kafka.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.backend.trade.adapter.kafka.dto.KafkaMessageDto;
import io.edpn.backend.trade.adapter.kafka.dto.mapper.KafkaMessageMapper;
import io.edpn.backend.trade.application.domain.Message;
import io.edpn.backend.trade.application.port.outgoing.kafka.CreateTopicPort;
import io.edpn.backend.trade.application.port.outgoing.kafka.SendKafkaMessagePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
public class KafkaMessageSender implements SendKafkaMessagePort {

    private final CreateTopicPort createTopicPort;
    private final KafkaMessageMapper messageMapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, JsonNode> jsonNodekafkaTemplate;


    @Override
    public Boolean send(Message message) {
        KafkaMessageDto kafkaMessageDto = messageMapper.map(message);
        return createTopicPort.createTopicIfNotExists(kafkaMessageDto.topic())
                .thenCompose(v -> {
                    try {
                        JsonNode jsonNodeMessage = objectMapper.readTree(kafkaMessageDto.message());
                        jsonNodekafkaTemplate.send(kafkaMessageDto.topic(), jsonNodeMessage);
                        return CompletableFuture.completedFuture(true);
                    } catch (JsonProcessingException e) {
                        log.error("Unable to send message to Kafka", e);
                        return CompletableFuture.completedFuture(false);
                    }
                }).exceptionally(ex -> {
                    log.error("Error while creating topic or sending message", ex);
                    return false;
                }).join();
    }
}