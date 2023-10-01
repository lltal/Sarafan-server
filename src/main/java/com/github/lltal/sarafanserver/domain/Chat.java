package com.github.lltal.sarafanserver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
})
@NamedEntityGraph(
        name = "Chat.detail",
        attributeNodes = {
                @NamedAttributeNode("messages")
        })
public class Chat {
    @Id
    @Column(name = "id")
    private String id;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;
}
