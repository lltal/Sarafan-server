package com.github.lltal.sarafanserver.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WebAgentSessionRegistry {

    private final Map<String, List<String>> sessionRegistry;

    public WebAgentSessionRegistry() {
        this.sessionRegistry = new HashMap<>();
    }

    public void addChat(String chatId, String agentId){
        synchronized (sessionRegistry) {
            if (sessionRegistry.containsKey(chatId)){
                sessionRegistry.get(chatId).add(agentId);
            } else {
                sessionRegistry.put(chatId, List.of(agentId));
            }
        }
    }

    public void removeChat(String chatId){
        synchronized (sessionRegistry) {
            sessionRegistry.remove(chatId);
        }
    }
}
