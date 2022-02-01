package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.repository.MessageRepository;
import ru.job4j.repository.PersonRepository;

/**
 * main service
 */
@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final PersonRepository personRepository;

    public ChatService(MessageRepository messageRepository, PersonRepository personRepository) {
        this.messageRepository = messageRepository;
        this.personRepository = personRepository;
    }
}
