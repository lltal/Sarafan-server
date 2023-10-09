package com.github.lltal.sarafanserver.utils;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lltal.sarafanserver.dto.EventType;
import com.github.lltal.sarafanserver.dto.WsMessageDto;
import com.github.lltal.sarafanserver.dto.ObjectType;
import com.github.lltal.sarafanserver.ifc.TripleConsumer;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WsSenderToSession {

    private final SimpMessagingTemplate template;
    private final ObjectMapper mapper;

    public WsSenderToSession(SimpMessagingTemplate template, ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    public <T> TripleConsumer<EventType, T, String> getWsSender(ObjectType objectType, Class<?> view){
        mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(view);
        return (EventType eventType, T payload, String sessionId) -> {
            String value = null;
            try {
                if (!(payload instanceof String))
                    value = mapper.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            log.info("receive T type={}", payload);
            template.convertAndSend("", new WsMessageDto(objectType, eventType, value));
            template.convertAndSendToUser(sessionId, "topic/private", new WsMessageDto(objectType, eventType, value));
        };
    }
}