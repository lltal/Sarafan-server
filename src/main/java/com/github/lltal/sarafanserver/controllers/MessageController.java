package com.github.lltal.sarafanserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.lltal.sarafanserver.annotations.CurrentUser;
import com.github.lltal.sarafanserver.domain.Chat;
import com.github.lltal.sarafanserver.domain.Message;
import com.github.lltal.sarafanserver.domain.Views;
import com.github.lltal.sarafanserver.security.UserPrincipal;
import com.github.lltal.sarafanserver.services.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/chats/{chatId}/messages")
public class MessageController
{
    @Autowired
    private MessageService messageService;

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> getMessages(@PathVariable("chatId") String chatId){
        return messageService.findAllMessagesInChat(chatId);
    }

    @GetMapping("/{messageId}")
    @JsonView(Views.IdName.class)
    public Message getMessageById(
            @PathVariable("chatId") String chatId,
            @PathVariable("messageId") long messageId
    ) {
        return messageService.findMessageInChat(chatId, messageId);
    }

    @PostMapping
    @JsonView(Views.IdName.class)
    public Message postMessage(
            @PathVariable("chatId") Chat chat,
            @RequestBody Message message,
            @CurrentUser UserPrincipal principal
    ) {
        return messageService.createMessage(chat, message, principal);
    }

    @PutMapping("/{messageId}")
    @JsonView(Views.IdName.class)
    public Message putMessage(
            @PathVariable("chatId") String chatId,
            @PathVariable("messageId") Message messageFromDb,
            @RequestBody Message message
    ){
        return messageService.updateMessage(chatId, messageFromDb, message);
    }

    @DeleteMapping("/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @JsonView(Views.IdName.class)
    public void deleteMessage(
            @PathVariable("chatId") String chatId,
            @PathVariable("messageId") Message message
    ){
        messageService.deleteMessageInChat(chatId, message);
    }
}