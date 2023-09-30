package com.github.lltal.sarafanserver.controllers;

import com.github.lltal.sarafanserver.domain.Chat;
import com.github.lltal.sarafanserver.repo.ChatRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/chats")
public class ChatController {

    private final ChatRepo chatRepo;

    public ChatController(ChatRepo chatRepo) {
        this.chatRepo = chatRepo;
    }

    @GetMapping
    public List<Chat> getChats(){
        return chatRepo.findAll();
    }

    @PostMapping("/{chatId}")
    public Chat postChat(
            @RequestBody Chat chat
    ) {
        Optional<Chat> chatFromDb = chatRepo.findById(chatId);
        return chatFromDb.orElseGet(() -> chatRepo.save(chat));
    }

    @DeleteMapping("/{chatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable("chatId") Chat chat){
        chatRepo.delete(chat);
    }
}
