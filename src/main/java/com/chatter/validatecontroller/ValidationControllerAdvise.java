package com.chatter.validatecontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контроллер будет отлавливать MethodArgumentNotValidException получаемы по результатам валидации
 * Если валидация не проходит (поле объекта + метод @Valid),
 * то в контексте приложения кидается исключение MethodArgumentNotValidException,
 * поэтому для того чтобы возвращать понятные ответы клиенту нужно его отлавливать.
 * Воспользуемся одним из способов - @ControllerAdivise + @ExceptionHandler.
 * В случае ошибки в данных, мы возвращаем список из объектов, который содержит имя поля,
 * сообщение об ошибке с указанием текущего значения.
 */
@ControllerAdvice
public class ValidationControllerAdvise {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
                e.getFieldErrors().stream()
                        .map(f -> Map.of(
                                f.getField(),
                                String.format("%s. Actual value: %s", f.getDefaultMessage(), f.getRejectedValue())
                        ))
                        .collect(Collectors.toList())
        );
    }
}