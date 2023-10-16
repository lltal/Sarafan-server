package com.github.lltal.sarafanserver.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
// TODO: add chatId in message primary key
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private long id;

    @JsonView(Views.IdName.class) private String text;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH-mm-ss")
    @JsonView(Views.IdName.class)
    private LocalDateTime creationDate;

    @JsonView(Views.IdName.class)
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User user;

    @JsonView(Views.FullMessage.class)
    @ManyToOne
    @JoinColumn(name = "chat_id", updatable = false, nullable = false)
    private Chat chat;
}