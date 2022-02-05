package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.entity.Message;

import ru.job4j.service.MessageService;

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
 */
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
    public ResponseEntity<Message> findById(@PathVariable int id) {
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

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        if (message.getDescription() == null) {
            throw new NullPointerException("Message Person mustn't be empty!");
        }
        return new ResponseEntity<>(
                this.messageService.save(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        if (message.getDescription() == null) {
            throw new NullPointerException("Message Person mustn't be empty!");
        }
        this.messageService.save(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
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
}

