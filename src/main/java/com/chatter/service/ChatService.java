package com.chatter.service;

import org.springframework.stereotype.Service;
import com.chatter.entity.Message;
import com.chatter.entity.Person;
import com.chatter.repository.MessageRepository;
import com.chatter.repository.PersonRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * гибридный service
 */
@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final PersonRepository personRepository;

    public ChatService(MessageRepository messageRepository, PersonRepository personRepository) {
        this.messageRepository = messageRepository;
        this.personRepository = personRepository;
    }

    public Iterable<Message> findAllMsg() {
        return messageRepository.findAll();
    }

    public Optional<Message> findByIdMsg(int id) {
        return messageRepository.findById(id);
    }

    public Message saveMsg(Message message) {
        message.setCreated(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public void deleteMsg(Message message) {
        messageRepository.delete(message);
    }

    public Iterable<Person> findAllPrs() {
        return personRepository.findAll();
    }

    public Optional<Person> findByIdPrs(int id) {
        return personRepository.findById(id);
    }

    public Person savePrs(Person person) {
        return personRepository.save(person);
    }

    public void deletePrs(Person person) {
        personRepository.delete(person);
    }

    public void saveMsg(Message message, int id) {
        var prs = personRepository.findById(id);
        var msg = messageRepository.save(message);
        prs.get().addMessage(msg);
    }
}
