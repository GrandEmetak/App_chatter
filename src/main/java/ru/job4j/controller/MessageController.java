package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.entity.Message;

import ru.job4j.entity.Operation;
import ru.job4j.service.MessageService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * для работы с сообщениями - Message Obkect - CRUD Operation
 * В методах поиска по id добавлен выброс исключения со статусом HttpStatus.NOT_FOUND в случае,
 * если данные не найдены.
 * + все Exception на null будет отлвливать глобавльный контролеер
 * -@ControllerAdvise *
 * аннотация @ControllerAdvise используемая совместно с @ExceptionHandler.
 * Код контроллера обрабатывает все исключения NullPointerException,
 * которые возникают во всех контроллерах:
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
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public List<Message> findAll() {
        return StreamSupport.stream(
                this.messageService.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable("id") @Min(1) int id) {
        if (id == 0) {
            throw new NullPointerException(" id message Person mustn't be empty!");
        }
        var message = this.messageService.findById(id);
        if (message.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Message is not found. Please, check requisites id.");
        }
        return new ResponseEntity<>(
                message.get(), HttpStatus.OK);
    }

    /**
     * аннотация в методе @Valid указывает, что предварительно перед тем как мы сможем работать с
     * моделью данные будут проходить валидацию согласно аннотациям валидации,
     * прописанным в модели данных Message
     *  - @Validated(Operation.OnCreate.class) то что id объекта будет - 0
     * @param message
     * @return
     */
    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Message> create(@Valid @RequestBody Message message) {
        if (message.getDescription().isEmpty()) {
            throw new NullPointerException("Message Person mustn't be empty!");
        }
        return new ResponseEntity<>(
                this.messageService.save(message),
                HttpStatus.CREATED
        );
    }

    /**
     * Update data model Message
     * аннотация в методе @Valid указывает, что предварительно перед тем как мы сможем работать с
     * моделью данные будут проходить валидацию согласно аннотациям валидации,
     * прописанным в модели данных Message
     * @param message
     * @return
     */
    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Message message) {
        if (message.getDescription().isEmpty()) {
            throw new NullPointerException("Message Person mustn't be empty!");
        }
        this.messageService.save(message);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete data model Message
     * так же использована аннтотаци Группы валидаций
     * -@Validated(Operation.OnDelete.class)
     * некоторые валидации должны срабатывать при различных обстоятельствах:
     * в указанном случае - только перед удалением
     * @param id Message object
     * @return
     */
    @DeleteMapping("/{id}")
    @Validated(Operation.OnDelete.class)
    public ResponseEntity<Void> delete(@Valid @PathVariable int id) {
        if (id == 0) {
            throw new NullPointerException(" id message Person mustn't be empty!");
        }
        var message = this.messageService.findById(id);
        if (message.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Message is not found. Please, check requisites id.");
        }
        this.messageService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * DTO
     * метод HTTP PATCH, который предназначен для частичного обновления данных.
     * метод для обновления не нулевых полей адреса.
     * Для этого использована воспользовать рефлексии для вызова нужных геттеров и сеттеров.
     *
     * @param message
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PatchMapping("/example2")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Message> example2(
            @Valid @RequestBody Message message) throws InvocationTargetException, IllegalAccessException {
        var current = messageService.findById(message.getId());
        if (current.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Message is not found. Please, check requisites id.");
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
                var newValue = getMethod.invoke(message);

                if (newValue != null) {
                    setMethod.invoke(current.get(), newValue);
                }
            }
        }
        return new ResponseEntity<>(
                this.messageService.save(message),
                HttpStatus.OK);
    }
}

