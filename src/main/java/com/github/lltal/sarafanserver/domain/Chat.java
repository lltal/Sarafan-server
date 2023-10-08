package com.github.lltal.sarafanserver.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
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
    @JsonView(Views.IdName.class)
    private String id;

    @JsonView(Views.IdName.class)
    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    @ManyToMany
    @JoinTable(
            name = "chat_user",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonView(Views.FullMessage.class)
    private List<User> users = new ArrayList<>();
}
