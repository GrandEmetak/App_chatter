package ru.job4j.handrelers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * аннотация @ControllerAdvise используемая совместно с @ExceptionHandler.
 * Код ниже обрабатывает все исключения NullPointerException, которые возникают во всех контроллерах:
 * Обработка исключений возникающих во многих контроллерах на предмет NPE -NullPointerException.class
 * в данном случае - Во всех контроллерах добавлена валидацию (проверки на null данных переданных пользователем)
 * входных данных.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class.getSimpleName());

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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
    @ExceptionHandler(value = {NullPointerException.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", "Some of fields empty");
                put("details", e.getMessage());
            }
        }));
        LOGGER.error(e.getMessage());
    }

}