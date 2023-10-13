package com.github.lltal.sarafanserver.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Chat {
    @Id
    @Column(name = "id")
    @JsonView(Views.Id.class)
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
