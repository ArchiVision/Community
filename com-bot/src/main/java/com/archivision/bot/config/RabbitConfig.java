package com.archivision.bot.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public Queue likesQueue() {
        return new Queue("like-events", true);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue("payment-events", true);
    }
}
