package com.github.lltal.sarafanserver.services;

import com.github.lltal.sarafanserver.domain.Chat;
import com.github.lltal.sarafanserver.domain.Message;
import com.github.lltal.sarafanserver.domain.User;
import com.github.lltal.sarafanserver.domain.Views;
import com.github.lltal.sarafanserver.dto.EventType;
import com.github.lltal.sarafanserver.dto.ObjectType;
import com.github.lltal.sarafanserver.exceptions.ResourceNotFoundException;
import com.github.lltal.sarafanserver.repo.MessageRepo;
import com.github.lltal.sarafanserver.repo.UserRepo;
import com.github.lltal.sarafanserver.security.UserPrincipal;
import com.github.lltal.sarafanserver.utils.KafkaSender;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MessageService {
    private final MessageRepo messageRepo;

    private final UserRepo userRepo;

    private final KafkaSender kafkaSender;

    public MessageService(
            MessageRepo messageRepo,
            UserRepo userRepo, KafkaSender sender
    ) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.kafkaSender = sender;
    }

    public List<Message> findAllMessagesInChat(String chatId){
        return messageRepo.findAllByChatId(chatId);
    }

    public Message findMessageInChat(String chatId, long messageId){
        return messageRepo.findMessageByChatIdAndId(chatId, messageId);
    }

    public Message createMessage(Chat chat, Message message, UserPrincipal principal){
        User user = userRepo.findById(principal.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("user", "id", principal.getId()));
        message.setUser(user);
        message.setCreationDate(LocalDateTime.now());
        message.setChat(chat);
        Message updatedMessage = messageRepo.save(message);
        kafkaSender.sendMessage(ObjectType.MESSAGE, Views.IdName.class, EventType.CREATE, updatedMessage, chat.getId());

        return updatedMessage;
    }

    public Message updateMessage(String chatId, Message messageFromDb, Message message) {
        BeanUtils.copyProperties(message, messageFromDb, "id");
        Message updatedMessage = messageRepo.save(messageFromDb);
        kafkaSender.sendMessage(ObjectType.MESSAGE, Views.IdName.class, EventType.UPDATE, updatedMessage, chatId);

        return updatedMessage;
    }

    public void deleteMessageInChat(String chatId, Message message){
        kafkaSender.sendMessage(ObjectType.MESSAGE, Views.IdName.class, EventType.REMOVE, message, chatId);
        messageRepo.delete(message);
    }
}
