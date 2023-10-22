package com.github.lltal.sarafanserver.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.lltal.sarafanserver.enums.AuthProvider;


import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Id.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(Views.IdName.class)
    private String name;

    @JsonView(Views.IdName.class)
    private String imageUrl;

    @Email
    @Column(nullable = false)
    @JsonView(Views.FullMessage.class)
    private String email;

    @Column(nullable = false)
    @JsonView(Views.FullMessage.class)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonView(Views.FullMessage.class)
    private AuthProvider provider;

    @JsonView(Views.FullMessage.class)
    private String providerId;

    @ManyToMany(mappedBy = "users")
    @JsonView(Views.FullMessage.class)
    private List<Chat> chats;
}
