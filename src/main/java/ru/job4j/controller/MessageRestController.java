package ru.job4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.entity.Message;
import ru.job4j.entity.ReportMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * RestTemplate - позволяет осуществлять вывозы стороннего REST API.
 * в нем мы объявили поле RestTemplate rest с аннотацией @Autowired,
 * поле будет проинициализировано значением бина,
 * который мы объявили ранее в Main class-Job4jAuthApplication.
 * акже мы объявили 2 константы, которые мы будем использовать
 * далее для реализации методов в нашем RestController.
 */
@RestController
@RequestMapping("/messagereport")
public class MessageRestController {
    // TODO дописать описание в методах
    private static final String API = "http://localhost:8080/messages/";

    private static final String API_ID = "http://localhost:8080/messages/{id}";

    @Autowired
    private RestTemplate rest;

    /**
     * GET /employee/ - получить список employees.
     * метод для получения отчета со всем списком Employee
     *
     * curl -i http://localhost:8080/messagereport/
     *
     * @return List<ReportEmployee>
     */
    @GetMapping("/")
    public List<ReportMessage> findAll() {
        List<ReportMessage> rsl = new ArrayList<>();
        List<Message> messages = rest.exchange(API,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Message>>() {
                }
        ).getBody();
        for (Message message : messages) {
            ReportMessage reportMsgs = ReportMessage.of(1, "First", message);
            rsl.add(reportMsgs);
        }
        return rsl;
    }

    /**
     * -@PostMapping("/")
     * POST /person/ - создать new Message.
     * запрос имеет вид одной строкой
     * curl -H 'Content-Type: application/json' -X POST -d
     * '{"name":"Petr","surname":"Shirokov","inn":"0258741"}' http://localhost:8080/employeereport/
     * String API -Переменная API_ID - содержит параметре {id}, который проставляется в аргументах метода.
     * Spring запущен и происходит запрос Hibernate
     * Hibernate: insert into person (login, password) values (?, ?)
     * что в консоли GitBush
     * Dload  Upload   Total   Spent    Left  Speed
     * 100    95    0    51  100    44    117    101 --:--:-- --:--:-- --:--:--
     * 219{"id":5,"login":"job4j@gmail.com","password":"123"}
     * @param message
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
       Message rsl = rest.postForObject(API, message, Message.class);
        return new ResponseEntity<>(rsl, HttpStatus.CREATED);
    }

    /**
     * PUT /employee/ - обновить пользователя.
     * выполняем следующий запрос: одной строкой
     * curl -i -H 'Content-Type: application/json' -X PUT -d
     * '{"id":"4","name":"John","surname":"Conor","inn":"0000001"}' http://localhost:8080/employeereport/
     * !!!
     *  Важно понимать, что если такой записи, которую мы передаем в запросе не будет в БД,
     *  то вместо замены будет выполнена вставка.
     *
     * @param message
     * @return
     */
    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        rest.put(API, message);
        return ResponseEntity.ok().build();
    }


    /**
     * DELETE /person/ - удалить пользователя.
     * Выполним запрос, удаляя запись с id = 5:
     * curl -i -X DELETE http://localhost:8080/employeereport/4
     * private static final String API_ID = "http://localhost:8080/employee/{id}";
     * Переменная API_ID - содержит параметре {id}, который проставляется в аргументах метода.
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        rest.delete(API_ID, id);
        return ResponseEntity.ok().build();
    }
}
