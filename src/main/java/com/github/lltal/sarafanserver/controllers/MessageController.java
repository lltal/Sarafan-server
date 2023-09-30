package com.github.lltal.sarafanserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.lltal.sarafanserver.config.properties.KafkaProperties;
import com.github.lltal.sarafanserver.domain.Chat;
import com.github.lltal.sarafanserver.domain.Message;
import com.github.lltal.sarafanserver.domain.Views;
import com.github.lltal.sarafanserver.dto.MessageDto;
import com.github.lltal.sarafanserver.dto.EventType;
import com.github.lltal.sarafanserver.dto.ObjectType;
import com.github.lltal.sarafanserver.repo.ChatRepo;
import com.github.lltal.sarafanserver.repo.MessageRepo;
import com.github.lltal.sarafanserver.utils.KafkaSender;
import com.github.lltal.sarafanserver.utils.WsSender;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@RestController
@RequestMapping(path = "/chats/{chatId}/messages")
public class MessageController
{
    private final MessageRepo messageRepo;

    private final BiConsumer<EventType, Message> kafkaSender;


    public MessageController(
            MessageRepo messageRepo,
            KafkaSender sender
    ) {
        this.messageRepo = messageRepo;
        this.kafkaSender = sender.getKafkaSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> getMessages(@PathVariable("chatId") String chatId){
        return messageRepo.findAllByChatId(chatId);
    }

    @GetMapping("/{messageId}")
    public Message getMessageById(
            @PathVariable("chatId") String chatId,
            @PathVariable("messageId") long messageId
    ) {
        return messageRepo.findMessageByChatIdAndId(chatId, messageId);
    }

    @PostMapping
    public Message postMessage(
            @PathVariable("chatId") Chat chat,
            @RequestBody Message message
    ) {

        message.setCreationDate(LocalDateTime.now());
        message.setChat(chat);
        kafkaSender.accept(EventType.CREATE, message);
        return message;
    }

    @PutMapping("/{messageId}")
    public Message putMessage(
            @PathVariable("messageId") Message messageFromDb,
            @RequestBody Message message
    ){
        BeanUtils.copyProperties(message, messageFromDb, "id");
        kafkaSender.accept(EventType.UPDATE, message);
        return messageRepo.save(messageFromDb);
    }

    @DeleteMapping("/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable("messageId") Message message){
        kafkaSender.accept(EventType.REMOVE, message);
        messageRepo.delete(message);
    }
}
