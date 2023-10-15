package com.github.lltal.sarafanserver.consumers;

import com.github.lltal.sarafanserver.domain.Views;
import com.github.lltal.sarafanserver.dto.KafkaMessageDto;
import com.github.lltal.sarafanserver.dto.EventType;
import com.github.lltal.sarafanserver.dto.ObjectType;
import com.github.lltal.sarafanserver.ifc.TripleConsumer;
import com.github.lltal.sarafanserver.utils.WsSenderToSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageHandler implements MessageListener<String, KafkaMessageDto> {
    private final TripleConsumer<EventType, Object, String> wsSender;

    public MessageHandler(
            WsSenderToSession sender
    ) {
        this.wsSender = sender.getWsSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    @Override
    public void onMessage(ConsumerRecord<String, KafkaMessageDto> messageRecord) {
        log.info("messageRecord= {}", messageRecord);
        try{
            wsSender.accept(
                    messageRecord.value().getEventType(),
                    messageRecord.value().getPayload(),
                    messageRecord.value().getChatId());
        } catch (Exception e) {
            log.info("Exception in MessageHandler={}", (Object)e.getStackTrace());
        }

    }
}