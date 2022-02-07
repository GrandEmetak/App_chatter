package ru.job4j.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.job4j.entity.Message;

import java.util.List;

@Data
@EqualsAndHashCode
public class PersonDTO {
    @EqualsAndHashCode.Include
    private String username;
    private int roleId;
    private List<Message> roles;
}
