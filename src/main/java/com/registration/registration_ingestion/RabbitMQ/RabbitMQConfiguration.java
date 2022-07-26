package com.registration.registration_ingestion.RabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RabbitMQConfiguration {

    @Value("${user_verification_queue_name}")
    private String QUEUE_NAME;
    @Value("${user_verification_exchange}")
    private String EXCHANGE_NAME;
    @Value("${user_verification_routing_key}")
    private String ROUTING_KEY;

    @Bean
    String getEXCHANGE_NAME(){
        return EXCHANGE_NAME;
    }

    @Bean
    String getROUTING_KEY(){
        return ROUTING_KEY;
    }

    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange){
        return BindingBuilder
                .bind(queue)
                .to(topicExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
