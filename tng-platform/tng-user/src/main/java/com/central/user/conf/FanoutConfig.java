package com.central.user.conf;

import com.central.common.constant.RabbitMqConstant;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:交换机绑定队列
 * @author: wh
 * @date: 2020-04-24
 * Copyright © 2019-grape. All rights reserved.
 */
@Component
public class FanoutConfig {
    @Bean
    public Queue fanOutSaveQueue() {
        // 将普通队列绑定到死信队列交换机上
        Map<String, Object> args = new HashMap<>(2);
        args.put(RabbitMqConstant.DEAD_LETTER_EXCHANGE_KEY, RabbitMqConstant.DEAD_EXCHANGE_NAME);
        args.put(RabbitMqConstant.DEAD_LETTER_ROUTING_KEY, RabbitMqConstant.DEAD_ROUTING_KEY);
        Queue queue = new Queue(RabbitMqConstant.QUEUE_SAVE_NAME, true, false, false, args);
        return queue;
    }

    // 2.定义交换机
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(RabbitMqConstant.EXCHANGE_NAME);
    }

    // 3.队列与交换机绑定注册接口队列
    @Bean
    Binding bindingExchange(Queue fanOutSaveQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanOutSaveQueue).to(fanoutExchange);
    }

    /**
     * 配置死信队列
     *
     * @return
     */
    @Bean
    public Queue deadQueue() {
        Queue queue = new Queue(RabbitMqConstant.DEAD_QUENE_NAME, true);
        return queue;
    }

    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(RabbitMqConstant.DEAD_EXCHANGE_NAME);
    }

    @Bean
    public Binding bindingDeadExchange(Queue deadQueue, DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(RabbitMqConstant.DEAD_ROUTING_KEY);
    }
    @Bean("customContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(100);  //设置线程数
        factory.setMaxConcurrentConsumers(100); //最大线程数
        configurer.configure(factory, connectionFactory);
        return factory;
    }

}


