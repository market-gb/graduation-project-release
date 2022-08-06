package ru.nhp.user.services;

import org.springframework.stereotype.Service;
import ru.nhp.user.entites.MessageTextElement;
import ru.nhp.user.repositories.MessageRepository;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<MessageTextElement> getMessageByTitle(String title){
        return messageRepository.getByTitleContaining(title);
    }
}