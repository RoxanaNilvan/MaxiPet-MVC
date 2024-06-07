package com.onlineshop.maxipetbackend.config;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {
    public static final String QUEUE_NAME = "email_queue";

    @Bean
    public Queue emailQueue() {
        return new Queue(QUEUE_NAME);
    }
}
