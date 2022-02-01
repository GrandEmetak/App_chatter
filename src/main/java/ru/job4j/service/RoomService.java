package ru.job4j.service;

import org.springframework.stereotype.Service;

import ru.job4j.entity.Room;

import ru.job4j.repository.RoomRepository;

import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public Iterable<Room> findAll() {
        return repository.findAll();
    }

    public Optional<Room> findById(int id) {
        return repository.findById(id);
    }

    public Room save(Room room) {
        return repository.save(room);
    }

    public void delete(Room room) {
        repository.delete(room);
    }
}
