package ru.nhp.user.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.nhp.user.entites.MessageTextElement;
import ru.nhp.user.enums.EmailType;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final MessageService messageService;
    private final UserService userService;

    public EmailService(JavaMailSender mailSender, MessageService messageService, UserService userService) {
        this.mailSender = mailSender;
        this.messageService = messageService;
        this.userService = userService;
    }

    @Async
    public void sendMail(EmailType emailType, Map<String, Object> params, Collection<String> receivers) {
        for (EmailType type : EmailType.values()) {
            if (type.equals(emailType))
                receivers.forEach(receiver -> sendMailOfEmailType(receiver, params, type.getName()));
        }
    }

    private void sendMailOfEmailType(String to, Map<String, Object> params, String subjectOfTheLetter) {
        try {
            boolean textType = false;
            if (subjectOfTheLetter.contains("TRUE")) {
                subjectOfTheLetter = subjectOfTheLetter.substring(0, subjectOfTheLetter.indexOf(", TRUE"));
                textType = true;
            }
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(to);
            helper.setFrom("market-gb@gb.ru");
            helper.setSubject(subjectOfTheLetter);
            helper.setText(textBuilder(to, params, subjectOfTheLetter), textType);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("failed to send mail ", e);
        }
    }

    private String textBuilder(String to, Map<String, Object> params, String key) {
        StringBuilder messageText = new StringBuilder();
        List<MessageTextElement> textElements = messageService.getMessageByTitle("Приветствие");
        textElements.forEach(messageTextElement -> {
            messageText.append(messageTextElement.getText());
            if (messageTextElement.getText().contains("день, "))
                if (userService.findByEmail(to).isEmpty()) messageText.append(to);
                else messageText.append(Objects.requireNonNull(userService.findByEmail(to).orElse(null)).getUsername());
        });
        if (key.equals("Подтвердите ваш email")) messageText.append("<br />");
        else messageText.append("\n");
        textElements = messageService.getMessageByTitle(key);
        textElements.forEach(messageTextElement -> {
            messageText.append(messageTextElement.getText());
            if (messageTextElement.getText().contains("№"))
                messageText.append(params.get("orderId"));
            if (messageTextElement.getText().contains("на сумму "))
                messageText.append(params.get("price"));
            if (messageTextElement.getText().contains("token="))
                messageText.append(params.get("token").toString());
            if (messageTextElement.getText().contains("ссылке."))
                messageText.append("<br />");
        });
        if (key.equals("Подтвердите ваш email")) messageText.append("<br />");
        else messageText.append("\n");
        textElements = messageService.getMessageByTitle("Подпись");
        textElements.forEach(messageTextElement -> messageText.append(messageTextElement.getText()));
        return messageText.toString();
    }

}
