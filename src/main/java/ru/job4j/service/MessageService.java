package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.entity.Message;

import ru.job4j.repository.MessageRepository;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Iterable<Message> findAll() {
        return messageRepository.findAll();
    }

    public Optional<Message> findById(int id) {
        return messageRepository.findById(id);
    }

    public Message save(Message message) {
        message.setCreated(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public void delete(Message message) {
        messageRepository.delete(message);
    }
}
