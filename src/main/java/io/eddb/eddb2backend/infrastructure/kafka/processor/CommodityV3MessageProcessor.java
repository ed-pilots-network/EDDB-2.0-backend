package io.eddb.eddb2backend.infrastructure.kafka.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.eddb.eddb2backend.application.dto.eddn.CommodityMessage;
import io.eddb.eddb2backend.application.usecase.ReceiveCommodityMessageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.Semaphore;

@RequiredArgsConstructor
public class CommodityV3MessageProcessor implements EddnMessageProcessor<CommodityMessage.V3> {

    private final ReceiveCommodityMessageUseCase receiveCommodityMessageUsecase;
    private final Semaphore semaphore = new Semaphore(1); // Change the number of permits if needed

    @Override
    @KafkaListener(topics = "https___eddn.edcd.io_schemas_commodity_3", groupId = "commodity", containerFactory = "eddnCommodityKafkaListenerContainerFactory")
    public void listen(JsonNode json) throws JsonProcessingException, InterruptedException {
        semaphore.acquire(); // Acquire a permit before processing the message
        try {
            handle(processJson(json));
        } finally {
            semaphore.release(); // Release the permit after processing is complete
        }
    }

    @Override
    public void handle(CommodityMessage.V3 message) {
        receiveCommodityMessageUsecase.receive(message);
    }

    @Override
    public CommodityMessage.V3 processJson(JsonNode json) throws JsonProcessingException {
        return new ObjectMapper().treeToValue(json, CommodityMessage.V3.class);
    }
}
