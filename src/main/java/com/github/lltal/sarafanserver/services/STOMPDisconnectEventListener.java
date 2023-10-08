package com.github.lltal.sarafanserver.services;

import com.github.lltal.sarafanserver.utils.WebAgentSessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Service
public class STOMPDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    @Autowired
    private WebAgentSessionRegistry webAgentSessionRegistry;
    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String chatId = sha.getNativeHeader("chatId").get(0);
        String sessionId = sha.getSessionId();
        webAgentSessionRegistry.removeSession(chatId, sessionId);
    }
}
