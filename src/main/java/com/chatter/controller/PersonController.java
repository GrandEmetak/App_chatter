package com.chatter.controller;

import com.chatter.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.chatter.entity.model.Operation;
import com.chatter.entity.model.Person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Controller Person Object
 * В методах поиска по id добавлен выброс исключения со статусом HttpStatus.NOT_FOUND в случае,
 * если данные не найдены.
 * + все Exception на null будет отлвливать глобавльный контролеер
 * -@ControllerAdvise *
 * аннотация @ControllerAdvise используемая совместно с @ExceptionHandler.
 * Код контроллера обрабатывает все исключения NullPointerException,
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
@RequestMapping("/persons")
public class PersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());

    private final PersonService personService;
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    public PersonController(PersonService personService, BCryptPasswordEncoder encoder, ObjectMapper objectMapper) {
        this.personService = personService;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return StreamSupport.stream(
                this.personService.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable("id") @Min(1) int id) {
            if (id == 0) {
                throw new NullPointerException("Person id mustn't be empty!");
            }
            var person = this.personService.findById(id);
            if (person.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "The Person is not found. Please, check requisites id.");
            }
            return new ResponseEntity<Person>(person.get(), HttpStatus.OK);
        }


    /**
     * create Object Message
     * Validation что id объекта на момент создания -0,
     * поля username and password not empty
     *
     * @param person Object
     * @return
     */
    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        if (person.getUsername().isEmpty() || person.getPassword() == null) {
            throw new NullPointerException("Person username and password mustn't be empty!");
        }
        return new ResponseEntity<>(
                this.personService.save(person),
                HttpStatus.CREATED
        );
    }

    /**
     * Update object Person
     * Validation object field id not null,
     * username and password not empty
     *
     * @param person
     * @return
     */
    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        if (person.getUsername().isEmpty() || person.getPassword() == null) {
            throw new NullPointerException("Person username and password mustn't be empty!");
        }
        this.personService.save(person);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete Person object
     * Validation object field id not null
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Validated(Operation.OnDelete.class)
    public ResponseEntity<Void> delete(@Valid @PathVariable int id) {
        if (id == 0) {
            throw new NullPointerException("Person id mustn't be empty");
        }
        var rsl = personService.findById(id);
        if (rsl.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Person is not found. Please, check requisites id.");
        }
        this.personService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * регистрация
     * + валидация на пустоя поле пароль/имя
     *
     * @param person
     * @return
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Person> signUp(@Valid @RequestBody Person person) {
        if (person.getPassword() == null || person.getUsername().isEmpty()) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        if (person.getPassword().length() < 3) {
            throw new IllegalArgumentException("Invalid Person parameter of field password");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        return new ResponseEntity<>(
                this.personService.save(person),
                HttpStatus.CREATED
        );
    }

    /**
     * Раздача файла = Правила чата для пользователей PDF/pdf general chat rules
     *
     * @return pdf general chat rules.pdf
     * @throws IOException
     */
    @GetMapping("/personrules")
    public ResponseEntity<byte[]> example5() throws IOException {
        var content = Files.readAllBytes(Path.of("./pdf general chat rules.pdf"));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(content.length)
                .body(content);
    }

    /**
     * DTO
     * метод HTTP PATCH, который предназначен для частичного обновления данных.
     * метод для обновления не нулевых полей адреса.
     * Для этого использована воспользовать рефлексии для вызова нужных геттеров и сеттеров.
     *
     * @param person
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PatchMapping("/example2")
    public ResponseEntity<Person> example2(@RequestBody Person person) throws InvocationTargetException, IllegalAccessException {
        var current = personService.findById(person.getId());
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
                var newValue = getMethod.invoke(person);

                if (newValue != null) {
                    setMethod.invoke(current.get(), newValue);

                }
            }
        }
        return new ResponseEntity<>(
                this.personService.save(person),
                HttpStatus.OK);
    }

    /**
     * value = { IllegalArgumentException.class } указывает, что обработчик будет обрабатывать
     * только данное исключение. Можно перечислить их больше, т.к. value это массив.
     * <p>
     * Метод, помеченный как @ExceptionHandler, поддерживает внедрение аргументов и возвращаемого типа
     * в рантайме, указанных в спецификации. По этому мы можем внедрить запрос, ответ и само исключение,
     * чтобы прописать какую-либо логику.
     * <p>
     * В данном случае при возникновении исключения IllegalArgumentException, метод exceptionHandler()
     * отлавливает его и меняет ответ, а именно его статус и тело. Также в последней строке
     * происходит логгирование.
     *
     * @param e
     * @param request
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", e.getMessage());
                put("type", e.getClass());
            }
        }));
        LOGGER.error(e.getLocalizedMessage());
    }

}
