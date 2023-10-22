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
    @Query(value = "select * from chat" +
            " join chat_user cu on cu.chat_id = chat.id" +
            " join users  on users.id = cu.user_id" +
            " where users.id = :userId", nativeQuery = true)
    List<Chat> findAllByUserId(@Param("userId") long userId);

    @EntityGraph(value = "Chat.detail", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Chat> findById(@Param("chatId") String chatId);
}
