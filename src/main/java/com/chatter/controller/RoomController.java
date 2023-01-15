package com.chatter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.chatter.entity.Operation;
import com.chatter.entity.Room;
import com.chatter.service.RoomService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
 * которые возникают во всех контроллерах:
 * + использован на уровне контроллера
 * - @ExceptionHandler
 * Данная аннотация позволяет отслеживать и обрабатывать исключения на уровне класса.
 * Если использовать ее например в контроллере, то исключения только данного контроллера будут обрабатываться.
 * +
 * Достаточно добавить в параметр input аннотацию @Valid,
 * чтобы сообщить спрингу передать объект Валидатору, прежде чем делать с ним что-либо еще.
 * Исключение MethodArgumentNotValidException выбрасывается, когда объект не проходит проверку.
 * По умолчанию, Spring переведет это исключение в HTTP статус 400.
 * + Группы валидаций - класс содержащий группы class Operation
 * их аннтотации перед методами @Validated(OnCreate.class) и тд
 * +
 * в случае валидации прямо в нутри параметров метода
 *  public ResponseEntity<Person> findById(@PathVariable("id") @Min(1) int id) {
 *  необходимо поставить аннотацию @Validated на уровне имени класса тогда Спринг будет знать что необходимо
 *  предварительно валидировать помеченные данные
 */
@Validated
@RestController
@RequestMapping("/rooms")
public class RoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class.getSimpleName());

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
    public ResponseEntity<Room> findById(@PathVariable("id") @Min(1) int id) {
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
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Room> create(@Valid @RequestBody Room room) {
        if (room.getName().isEmpty()) {
            throw new NullPointerException("The Room name mustn't be empty!");
        }
        return new ResponseEntity<>(
                this.roomService.save(room),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Room room) {
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
    @Validated(Operation.OnDelete.class)
    public ResponseEntity<Void> delete(@Valid @PathVariable int id) {
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
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Room> example2(@Valid @RequestBody Room room) throws InvocationTargetException, IllegalAccessException {
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
