package com.tigercel.hearthstone.config;

import com.tigercel.hearthstone.service.RabbitConsumer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by somebody on 2016/8/9.
 */
@Configuration
public class RabbitmqConfiguration {

    public final static String QUEUE_SEND_VCODE = "hearthstone-vcode";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public Queue sendEmailQueue() {
        return new Queue(QUEUE_SEND_VCODE, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("hearthstone-exchange");
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_SEND_VCODE);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_SEND_VCODE);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public RabbitConsumer receiver() {
        return new RabbitConsumer();
    }

    @Bean
    MessageListenerAdapter listenerAdapter(RabbitConsumer receiver) {
        MessageListenerAdapter m =  new MessageListenerAdapter(receiver);

        m.addQueueOrTagToMethodName(QUEUE_SEND_VCODE, "receiveMessage");
        m.setMessageConverter(new SerializerMessageConverter());

        return m;
    }
}
