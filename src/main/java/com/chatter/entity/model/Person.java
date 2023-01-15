package com.chatter.entity.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;

import java.util.List;

/**
 * Модель пользователя в чате
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Null(groups = Operation.OnCreate.class)
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class,
            Operation.OnDelete.class})
        private int id;

    @NotBlank(message = "User Name must be not empty")
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Password must be not empty")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToOne(cascade = {CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(cascade = {CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.PERSIST,
            CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private List<Message> messages = new ArrayList<>();

    public void addMessage(Message msg) {
        this.messages.add(msg);
    }
}
