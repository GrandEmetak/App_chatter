package ru.job4j.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

/**
 * DTO группирует все данные, необходимые для создания пользователя,
 *  и отправляет их на сервер в одном запросе, что оптимизирует взаимодействие с API,
 *  только необходимую информацию для создания Объекта клиент
 *  DTO представляет собой модель, отправляемую от клиента по API.
 */
@Data
@EqualsAndHashCode
public class PersonCreatedDTO {

    @EqualsAndHashCode.Include
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
}
