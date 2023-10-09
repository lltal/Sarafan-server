package com.github.lltal.sarafanserver.consumers;

import java.util.Set;

import com.github.lltal.sarafanserver.config.properties.KafkaProperties;
import com.github.lltal.sarafanserver.domain.Views;
import com.github.lltal.sarafanserver.dto.KafkaMessageDto;
import com.github.lltal.sarafanserver.dto.EventType;
import com.github.lltal.sarafanserver.dto.ObjectType;
import com.github.lltal.sarafanserver.ifc.TripleConsumer;
import com.github.lltal.sarafanserver.utils.WebAgentSessionRegistry;
import com.github.lltal.sarafanserver.utils.WsSenderToSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageHandler implements MessageListener<String, KafkaMessageDto> {
    private final TripleConsumer<EventType, Object, String> wsSender;
    private final WebAgentSessionRegistry sessionRegistry;

    public MessageHandler(
            WsSenderToSession sender,
            WebAgentSessionRegistry sessionRegistry
    ) {
        this.wsSender = sender.getWsSender(ObjectType.MESSAGE, Views.IdName.class);
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void onMessage(ConsumerRecord<String, KafkaMessageDto> messageRecord) {
        log.info("messageRecord= {}", messageRecord);
        try{
            sessionRegistry
                    .getSessionsByChatId(
                            messageRecord.value().getChatId())
                    .forEach(sessionId -> {
                        wsSender.accept(
                                messageRecord.value().getEventType(),
                                messageRecord.value(),
                                sessionId);
                    });
        } catch (Exception e) {
            log.info("Exception in MessageHandler={}", e.getStackTrace());
        }

    }
}
