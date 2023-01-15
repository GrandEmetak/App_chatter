package com.chatter.entity.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс представляет модель Сообщения в чате
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Null(groups = Operation.OnCreate.class)
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class,
            Operation.OnDelete.class})
    int id;

    @NotBlank(message = "Description yours message must be not empty")
    @Column(name = "description")
    String description;

    @Column(name = "created")
    LocalDateTime created;


    public static Message of(int id, String description) {
        Message message = new Message();
        message.id = id;
        message.description = description;
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Message message = (Message) o;
        return id != 0 && Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
