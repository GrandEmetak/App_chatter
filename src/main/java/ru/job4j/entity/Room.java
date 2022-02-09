package ru.job4j.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

/**
 * Комната чата
 * +
 * добавлены Аннотации валидации
 * - @NotBlank проверяет, что строка не пустая;
 * +
 * Достаточно добавить в параметр input аннотацию @Valid,
 * чтобы сообщить спрингу передать объект Валидатору, прежде чем делать с ним что-либо еще.
 * Исключение MethodArgumentNotValidException выбрасывается, когда объект не проходит проверку.
 * По умолчанию, Spring переведет это исключение в HTTP статус 400.
 * + Группы валидаций - класс содержащий группы class Operation
 * их аннтотации перед методами @Validated(OnCreate.class) и тд
 */

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Null(groups = Operation.OnCreate.class)
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class,
            Operation.OnDelete.class})
    private int id;

    @NotBlank(message = "Name Room must be not empty")
    @Column(name = "name")
    private String name;

    @OneToMany
    @JoinColumn(name = "room_id")
    private List<Person> persons = new ArrayList<>();

    public Room() {
    }

    public static Room of(int id, String roomName) {
        Room room = new Room();
        room.id = id;
        room.name = roomName;
        return room;
    }

    public Person add(Person person) {
        persons.add(person);
        return person;
    }
}

