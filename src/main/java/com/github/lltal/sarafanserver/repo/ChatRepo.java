package com.github.lltal.sarafanserver.repo;

import com.github.lltal.sarafanserver.domain.Chat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;

import java.util.List;
import java.util.Optional;

public interface ChatRepo extends JpaRepository<Chat, String> {

    @Override
    @EntityGraph(value = "Chat.detail", type = EntityGraph.EntityGraphType.LOAD)
    List<Chat> findAll();

    @Override
    @Query(value = "select * from chat" +
            " left outer join message on" +
            " message.chat_id = chat.id" +
            " where chat.id = :chatId", nativeQuery = true)
    Optional<Chat> findById(@Param("chatId") String chatId);
}
