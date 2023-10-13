package com.github.lltal.sarafanserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.lltal.sarafanserver.annotations.CurrentUser;
import com.github.lltal.sarafanserver.domain.Chat;
import com.github.lltal.sarafanserver.domain.Message;
import com.github.lltal.sarafanserver.domain.User;
import com.github.lltal.sarafanserver.domain.Views;
import com.github.lltal.sarafanserver.dto.EventType;
import com.github.lltal.sarafanserver.dto.KafkaMessageDto;
import com.github.lltal.sarafanserver.dto.ObjectType;
import com.github.lltal.sarafanserver.exceptions.ResourceNotFoundException;
import com.github.lltal.sarafanserver.ifc.TripleConsumer;
import com.github.lltal.sarafanserver.repo.MessageRepo;
import com.github.lltal.sarafanserver.repo.UserRepo;
import com.github.lltal.sarafanserver.security.UserPrincipal;
import com.github.lltal.sarafanserver.utils.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@Slf4j
@RestController
@RequestMapping(path = "/chats/{chatId}/messages")
public class MessageController
{
    private final MessageRepo messageRepo;

    private final UserRepo userRepo;

    private final KafkaSender kafkaSender;

    public MessageController(
            MessageRepo messageRepo,
            UserRepo userRepo, KafkaSender sender
    ) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.kafkaSender = sender;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> getMessages(@PathVariable("chatId") String chatId){
        return messageRepo.findAllByChatId(chatId);
    }

    @GetMapping("/{messageId}")
    @JsonView(Views.IdName.class)
    public Message getMessageById(
            @PathVariable("chatId") String chatId,
            @PathVariable("messageId") long messageId
    ) {
        return messageRepo.findMessageByChatIdAndId(chatId, messageId);
    }

    @PostMapping
    @Transactional
    @JsonView(Views.IdName.class)
    public Message postMessage(
            @PathVariable("chatId") Chat chat,
            @RequestBody Message message,
            @CurrentUser UserPrincipal principal
    ) {
        User user = userRepo.findById(principal.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("user", "id", principal.getId()));
        message.setUser(user);
        message.setCreationDate(LocalDateTime.now());
        message.setChat(chat);
        kafkaSender.sendMessage(ObjectType.MESSAGE, Views.IdName.class, EventType.CREATE, message, chat.getId());
        return messageRepo.save(message);
    }

    @PutMapping("/{messageId}")
    @JsonView(Views.IdName.class)
    public Message putMessage(
            @PathVariable("chatId") String chatId,
            @PathVariable("messageId") Message messageFromDb,
            @RequestBody Message message
    ){
        BeanUtils.copyProperties(message, messageFromDb, "id");
        kafkaSender.sendMessage(ObjectType.MESSAGE, Views.IdName.class, EventType.UPDATE, message, chatId);

        return messageRepo.save(messageFromDb);
    }

    @DeleteMapping("/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @JsonView(Views.IdName.class)
    public void deleteMessage(
            @PathVariable("chatId") String chatId,
            @PathVariable("messageId") Message message
    ){
        kafkaSender.sendMessage(ObjectType.MESSAGE, Views.IdName.class, EventType.REMOVE, message, chatId);
        messageRepo.delete(message);
    }
}