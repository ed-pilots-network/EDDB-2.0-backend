package io.edpn.backend.trade.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;

import io.edpn.backend.trade.infrastructure.kafka.KafkaTopicHandler;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration("TradeModuleKafkaConfig")
public interface KafkaConfig {

    @EnableKafka
    @Configuration("TradeModuleEddnJsonKafkaConsumerConfig")
    class EddnJsonKafkaConsumerConfig {
        @Value(value = "${trade.spring.kafka.bootstrap-servers}")
        private String bootstrapServers;

        public ConcurrentKafkaListenerContainerFactory<String, JsonNode> kafkaListenerContainerFactory(String groupId) {
            ConcurrentKafkaListenerContainerFactory<String, JsonNode> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory(groupId));
            return factory;
        }

        public ConsumerFactory<String, JsonNode> consumerFactory(String groupId) {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
            configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, JsonNode.class);
            configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
            return new DefaultKafkaConsumerFactory<>(configProps);
        }

        @Bean(name = "tradeModuleKafkaListenerContainerFactory")
        public ConcurrentKafkaListenerContainerFactory<String, JsonNode> kafkaListenerContainerFactory(EddnJsonKafkaConsumerConfig kafkaConfig) {
            return kafkaConfig.kafkaListenerContainerFactory("tradeModule");
        }
    }

    @Configuration("TradeModuleJsonNodeKafkaProducerConfig")
    class JsonNodeKafkaProducerConfig {
        @Value(value = "${trade.spring.kafka.bootstrap-servers}")
        private String bootstrapServers;

        @Bean(name = "tradeJsonNodeProducerFactory")
        public ProducerFactory<String, JsonNode> jsonNodeProducerFactory() {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean(name = "tradeJsonNodekafkaTemplate")
        public KafkaTemplate<String, JsonNode> jsonNodekafkaTemplate(@Qualifier("tradeJsonNodeProducerFactory") ProducerFactory<String, JsonNode> jsonNodeProducerFactory) {
            return new KafkaTemplate<>(jsonNodeProducerFactory);
        }
    }

    @Configuration("TradeModuleKafkaAdminConfig")
    class KafkaAdminConfig {
        @Value(value = "${trade.spring.kafka.bootstrap-servers}")
        private String bootstrapServers;

        @Bean(name = "tradeKafkaAdminClient")
        public AdminClient kafkaAdminClient() {
            Map<String, Object> configs = new HashMap<>();
            configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            return AdminClient.create(configs);
        }

        @Bean(name = "tradeKafkaTopicCreator")
        public KafkaTopicHandler kafkaTopicCreator(@Qualifier("tradeKafkaAdminClient") AdminClient adminClient,
                                                   @Value(value = "${trade.spring.kafka.topic.partitions:1}") final int topicPartitions,
                                                   @Value(value = "${trade.spring.kafka.topic.replicationfactor:1}") final short topicReplicationFactor) {
            return new KafkaTopicHandler(adminClient, topicPartitions, topicReplicationFactor);
        }
    }
}
