package com.github.lltal.sarafanserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.github.lltal.sarafanserver.config.properties.KafkaProperties;
import com.github.lltal.sarafanserver.dto.EventType;
import com.github.lltal.sarafanserver.dto.KafkaMessageDto;
import com.github.lltal.sarafanserver.dto.WsMessageDto;
import com.github.lltal.sarafanserver.dto.ObjectType;
import com.github.lltal.sarafanserver.ifc.TripleConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, KafkaMessageDto> kafkaTemplate;
    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private ObjectMapper mapper;

    public <T> void sendMessage(
            ObjectType objectType,
            Class<?> view,
            EventType eventType,
            T payload,
            String chatId
    ){
        String value = null;
        try {
            if (!(payload instanceof String))
                value = mapper.writerWithView(view).writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(kafkaProperties.getTopic(), new KafkaMessageDto(objectType, eventType, chatId, value));
    }

}
