package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.entity.Person;
import ru.job4j.entity.Room;
import ru.job4j.service.RoomService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * посмотр, все активные комнаты с их содержимым
 * В методах поиска по id добавлен выброс исключения со статусом HttpStatus.NOT_FOUND в случае,
 * если данные не найдены.
 * + все Exception на null будет отлвливать глобавльный контролеер
 * -@ControllerAdvise *
 * аннотация @ControllerAdvise используемая совместно с @ExceptionHandler.
 * Код контроллера обрабатывает все исключения NullPointerException,
 * которые возникают во всех контроллерах:
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public List<Room> findAll() {
        return StreamSupport.stream(
                this.roomService.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        if (id == 0) {
            throw new NullPointerException("The id Room mustn't be empty!");
        }
        var person = this.roomService.findById(id);
        return new ResponseEntity<>(
                person.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Room is not found. Please, check requisites."
                )), person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        if (room.getName().isEmpty()) {
            throw new NullPointerException("The Room name mustn't be empty!");
        }
        return new ResponseEntity<>(
                this.roomService.save(room),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        if (room.getName().isEmpty()) {
            throw new NullPointerException("The Room name mustn't be empty!");
        }
        var rsl = roomService.findById(room.getId());

        if (!rsl.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Room is not found. Please, check requisites.");
        }
        this.roomService.save(room);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаление Room Object By Id
     * В методах поиска по id добавлен выброс исключения со статусом HttpStatus.NOT_FOUND в случае,
     * если данные не найдены.
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id == 0) {
            throw new NullPointerException("The Room name mustn't be empty!");
        }
        var rsl = roomService.findById(id);
        if (rsl.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Room is not found. Please, check requisites id.");

        }
        this.roomService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * DTO
     * метод HTTP PATCH, который предназначен для частичного обновления данных.
     * метод для обновления не нулевых полей адреса.
     * Для этого использована воспользовать рефлексии для вызова нужных геттеров и сеттеров.
     * @param room
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PatchMapping("/example2")
    public ResponseEntity<Room> example2(@RequestBody Room room) throws InvocationTargetException, IllegalAccessException {
        var current = roomService.findById(room.getId());
        if (current.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Person is not found. Please, check requisites id.");
        }
        var methods = current.get().getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method : methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {

                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid properties mapping");
                }
                var newValue = getMethod.invoke(room);
                if (newValue != null) {
                    setMethod.invoke(current.get(), newValue);
                }
            }
        }
        return new ResponseEntity<>(
                this.roomService.save(room),
                HttpStatus.OK);
    }
}
