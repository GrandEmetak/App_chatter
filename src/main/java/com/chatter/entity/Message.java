package com.chatter.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

/**
 * Класс представляет модель Сообщения в чате
 * +
 * добавлены Аннотации валидации
 * - @NotBlank проверяет, что строка не пустая;
 * + @NotNull проверяет, что поле не null;
 * + Группы валидации
 * -@NotNull(message = "Id must be non null", groups = {
 * Operation.OnUpdate.class, Operation.OnDelete.class})
 * id - пуст присозданни, и полон при обновлении
 * при этом необходимо добавить в сам Контроллер где находятся методы
 * использующии эти данные аннтотацию @Validated
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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
    private int id;

    @NotBlank(message = "Description yours message must be not empty")
    private String description;

    private LocalDateTime created;

    public Message() {
    }

    public static Message of(int id, String description) {
        Message message = new Message();
        message.id = id;
        message.description = description;
        return message;
    }
}
