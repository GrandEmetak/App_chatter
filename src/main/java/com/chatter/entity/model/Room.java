package com.chatter.entity.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Комната чата
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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
   int id;

    @NotBlank(message = "Name Room must be not empty")
    @Column(name = "name")
    String name;

    @OneToMany
    @JoinColumn(name = "room_id")
    List<Person> persons = new ArrayList<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id
                && Objects.equals(name, room.name)
                && Objects.equals(persons, room.persons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

