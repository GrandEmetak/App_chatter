package ru.job4j.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;

import java.util.List;

/**
 * Модель пользователя в чате
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
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

    public Person() {
    }

    public static Person of(int id, String username, String password, boolean enabled) {
        Person person = new Person();
        person.id = id;
        person.username = username;
        person.password = password;
        person.enabled = enabled;
        return person;
    }

    public Message add(Message message) {
        this.messages.add(message);
        return message;
    }
}
