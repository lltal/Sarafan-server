package com.github.lltal.sarafanserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lltal.sarafanserver.config.properties.KafkaProperties;
import com.github.lltal.sarafanserver.dto.EventType;
import com.github.lltal.sarafanserver.dto.MessageDto;
import com.github.lltal.sarafanserver.dto.ObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, MessageDto> kafkaTemplate;
    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private ObjectMapper mapper;

    public <T> BiConsumer<EventType, T> getKafkaSender(ObjectType objectType, Class<?> view){
        mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(view);
        return (EventType eventType, T payload) -> {
            String value = null;
            try {
                if (!(payload instanceof String))
                    value = mapper.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            kafkaTemplate.send(kafkaProperties.getTopic(), new MessageDto(objectType, eventType, value));
        };
    }

}
