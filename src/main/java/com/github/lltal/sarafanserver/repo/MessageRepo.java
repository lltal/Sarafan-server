package com.github.lltal.sarafanserver.repo;

import com.github.lltal.sarafanserver.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {

    List<Message> findAllByChatId(String chatId);

    Message findMessageByChatIdAndId(String chatId, long id);
}
