package com.github.lltal.sarafanserver.config;

import com.github.lltal.sarafanserver.config.properties.KafkaProperties;
import com.github.lltal.sarafanserver.consumers.MessageHandler;
import com.github.lltal.sarafanserver.dto.MessageDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerConfiguration {

    @Autowired
    private KafkaProperties properties;

    @Autowired
    private MessageHandler messageHandler;

    private Map<String, Object> consumerConfig(){
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapBroker());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return config;
    }

    @Bean
    public KafkaMessageListenerContainer<String, MessageDto> messageListener()
    {
        ContainerProperties containerProperties = new ContainerProperties(properties.getTopic());
        containerProperties.setMessageListener(messageHandler);
        DefaultKafkaConsumerFactory<String, MessageDto> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerConfig());
        KafkaMessageListenerContainer<String, MessageDto> listenerContainer = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        // bean name is prefix to kafka consumer thread name
        listenerContainer.setBeanName("kafka-message-listener");
        return listenerContainer;
    }
}
