package com.github.lltal.sarafanserver.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class WebAgentSessionRegistry {

    private final Map<String, Set<String>> sessionRegistry;

    public WebAgentSessionRegistry() {
        this.sessionRegistry = new HashMap<>();
    }

    public void addSession(String chatId, String sessionId){
        synchronized (sessionRegistry) {
            if (sessionRegistry.containsKey(chatId)){
                sessionRegistry.get(chatId).add(sessionId);
            } else {
                sessionRegistry.put(chatId, Set.of(sessionId));
            }
        }
    }

    public void removeSession(String chatId, String sessionId){
        synchronized (sessionRegistry) {
            sessionRegistry.get(chatId).remove(sessionId);
        }
    }
}
