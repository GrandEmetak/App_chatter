package ru.job4j.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Комната чата
 */

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
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

