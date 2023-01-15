package com.chatter.entity.dto;

import com.chatter.entity.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class PersonDTO {
    @EqualsAndHashCode.Include
    private String username;
    private int roleId;
    private List<Message> roles;
}
