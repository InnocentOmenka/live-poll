package com.innocodes.live_poll.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "vote-queue";
    public static final String EXCHANGE_NAME = "vote-exchange";
    public static final String ROUTING_KEY = "vote";
    public static final String DLQ_NAME = "vote-dlq-queue";
    public static final String DLX_NAME = "vote-dlq-exchange";
    public static final String DLX_ROUTING_KEY = "vote-dlq";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true, false, false, Map.of(
                "x-dead-letter-exchange", DLX_NAME,
                "x-dead-letter-routing-key", DLX_ROUTING_KEY
        ));
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ_NAME, true);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_NAME);
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(DLX_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}