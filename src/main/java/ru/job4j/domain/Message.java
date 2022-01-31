package ru.job4j.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс представляет модель Сообщения в чате
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

//    private String name;

    private String description;

    private LocalDateTime created;

    public Message() {
    }

 //   String name,
    public  static Message of(int id, String description) {
        Message message = new Message();
        message.id = id;
//        message.name = name;
        message.description = description;
        return message;
    }
}
