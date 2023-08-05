package io.edpn.backend.trade.infrastructure.persistence.mappers.entity;

import io.edpn.backend.trade.domain.model.RequestDataMessage;
import io.edpn.backend.trade.infrastructure.persistence.entity.RequestDataMessageEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestDataMessageMapper {

    public RequestDataMessageEntity map(RequestDataMessage requestDataMessage) {
        String sanitizedTopicName = this.sanitizeTopicName(requestDataMessage.getTopic());
        return RequestDataMessageEntity.builder().topic(sanitizedTopicName).message(requestDataMessage.getMessage().asText()).build();
    }

    private String sanitizeTopicName(String topicName) {
        return topicName.replaceAll("[^A-Za-z0-9._\\-]", "_");
    }

}
