package com.onlineshop.maxipetbackend.config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineshop.maxipetbackend.dtos.NotificationRequestDTO;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitSender {
    private final RabbitTemplate rabbitTemplate;
    private final Queue emailQueue;
    private final ObjectMapper objectMapper;
    private final static String QUEUE_NAME = "coada2";

    @Autowired
    public RabbitSender(RabbitTemplate rabbitTemplate, Queue emailQueue, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.emailQueue = emailQueue;
        this.objectMapper = objectMapper;
    }

    public void send(NotificationRequestDTO notificationRequestDto) {
        try {
            String payload = objectMapper.writeValueAsString(notificationRequestDto);
            System.out.println("Sending message: " + payload);
            rabbitTemplate.convertAndSend(emailQueue.getName(), payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(NotificationRequestDTO notificationRequestDto) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String payload = objectMapper.writeValueAsString(notificationRequestDto);
            System.out.println("Sending message: " + payload);
            channel.basicPublish("", QUEUE_NAME, null, payload.getBytes());
            System.out.println(" [x] Sent '" + payload + "'");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
