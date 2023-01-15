package com.chatter.service;

import org.springframework.stereotype.Service;
import com.chatter.entity.model.Message;

import com.chatter.repository.MessageRepository;


import java.time.LocalDateTime;
import java.util.Optional;

/**
 * сервис сообщений
 */
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

    public void deleteById(int id) {
        messageRepository.deleteById(id);
    }
}
