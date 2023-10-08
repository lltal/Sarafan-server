package com.github.lltal.sarafanserver.consumers;

import com.github.lltal.sarafanserver.config.properties.KafkaProperties;
import com.github.lltal.sarafanserver.domain.Message;
import com.github.lltal.sarafanserver.domain.Views;
import com.github.lltal.sarafanserver.dto.MessageDto;
import com.github.lltal.sarafanserver.dto.EventType;
import com.github.lltal.sarafanserver.dto.ObjectType;
import com.github.lltal.sarafanserver.utils.WsSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
@Slf4j
public class MessageHandler implements MessageListener<String, MessageDto> {
    private final BiConsumer<EventType, Object> wsSender;

    public MessageHandler(WsSender sender, KafkaProperties properties) {
        this.wsSender= sender.getWsSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    @Override
    public void onMessage(ConsumerRecord<String, MessageDto> messageRecord) {
        log.info("messageRecord= {}", messageRecord);
        wsSender.accept(messageRecord.value().getEventType(), messageRecord.value());
    }
}
