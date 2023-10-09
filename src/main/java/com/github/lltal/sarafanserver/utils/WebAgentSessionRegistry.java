package com.github.lltal.sarafanserver.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebAgentSessionRegistry {

    private final Map<String, Set<String>> sessionRegistry;

    public WebAgentSessionRegistry() {
        this.sessionRegistry = new ConcurrentHashMap<>();
    }

    public void addSession(String chatId, String sessionId){
        synchronized (sessionRegistry) {
            if (sessionRegistry.containsKey(chatId)){
                sessionRegistry.get(chatId).add(sessionId);
            } else {
                sessionRegistry.put(chatId, new HashSet<>(){{add(sessionId);}});
            }
        }
    }

    public void removeSession(String chatId, String sessionId){
        synchronized (sessionRegistry) {
            sessionRegistry.get(chatId).remove(sessionId);
        }
    }

    public Set<String> getSessionsByChatId(String chatId){
        return sessionRegistry.get(chatId);
    }
}
