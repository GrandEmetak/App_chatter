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
 * Для возможности стороннего REST API.
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

    private static final String API = "http://localhost:8080/messages/";

    private static final String API_ID = "http://localhost:8080/messages/{id}";

    @Autowired
    private RestTemplate rest;

    /**
     * GET /messages/ - получить список persons.
     * метод для получения отчета со всем списком Message
     *
     * curl -i http://localhost:8080/messagereport/
     *
     * @return List<Report<Message>>
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
     * POST /message/ - создать new Message.
     * запрос имеет вид одной строкой
     * curl -H 'Content-Type: application/json' -X POST -d
     * '{"description":"Велопрогулка будет самым лучшым время препровождением в выходной"}' http://localhost:8080/messagereport/
     * String API -Переменная API_ID - содержит параметре {id}, который проставляется в аргументах метода.
     * Spring запущен и происходит запрос Hibernate
     * Hibernate: insert into message (description) values (?)
     * что в консоли GitBush
     * Dload  Upload   Total   Spent    Left  Speed
     * 100    95    0    51  100    44    117    101 --:--:-- --:--:-- --:--:--
     * 219{"id":5,"description":"Велопрогулка будет самым лучшым время препровождением в выходной",
     * "created":"2022-02-01T10:13:01.110276"}
     * @param message
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
       Message rsl = rest.postForObject(API, message, Message.class);
        return new ResponseEntity<>(rsl, HttpStatus.CREATED);
    }

    /**
     * PUT /message/ - обновить пользователя.
     * выполняем следующий запрос: одной строкой
     * curl -i -H 'Content-Type: application/json' -X PUT -d
     * '{"description":"Have a nice day)))"}' http://localhost:8080/messagereport/
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
     * DELETE /message/ - удалить пользователя.
     * Выполним запрос, удаляя запись с id = 5:
     * curl -i -X DELETE http://localhost:8080/messagereport/4
     * private static final String API_ID = "http://localhost:8080/message/{id}";
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
