package com.github.lltal.sarafanserver.services;

import com.github.lltal.sarafanserver.utils.WebAgentSessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Service
public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    private WebAgentSessionRegistry webAgentSessionRegistry;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        String agentId = sha.getNativeHeader("agentId").get(0);
        String chatId = sha.getNativeHeader("chatId").get(0);

        /** add new session to registry */
        webAgentSessionRegistry.addChat(chatId, agentId);
    }
}
