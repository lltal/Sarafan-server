package com.github.lltal.sarafanserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.lltal.sarafanserver.annotations.CurrentUser;
import com.github.lltal.sarafanserver.domain.Chat;
import com.github.lltal.sarafanserver.domain.User;
import com.github.lltal.sarafanserver.domain.Views;
import com.github.lltal.sarafanserver.exceptions.ResourceNotFoundException;
import com.github.lltal.sarafanserver.repo.ChatRepo;
import com.github.lltal.sarafanserver.repo.UserRepo;
import com.github.lltal.sarafanserver.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/chats")
public class ChatController {

    private final ChatRepo chatRepo;
    private final UserRepo userRepo;

    public ChatController(ChatRepo chatRepo, UserRepo userRepo) {
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Chat> getChats(@CurrentUser UserPrincipal principal){
        return chatRepo.findAllByUserId(principal.getId());
    }

    @GetMapping("/{chatId}")
    @JsonView(Views.IdName.class)
    public Chat getChatById(
            @PathVariable("chatId") String chatId
    ){
        return chatRepo
                .findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("chat", "chatId", chatId));
    }

    @PostMapping
    @JsonView(Views.IdName.class)
    public Chat postChat(
            @RequestBody Chat chat
    ) {
        List<User> usersById = userRepo.findAllById(Arrays
                .stream(chat
                        .getId()
                        .split("-"))
                .map(Long::parseLong)
                .toList());

        usersById.forEach(user -> {
            chat.getUsers().add(user);
        });

        return chatRepo.save(chat);
    }

    @DeleteMapping("/{chatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable("chatId") Chat chat){
        chatRepo.delete(chat);
    }
}
