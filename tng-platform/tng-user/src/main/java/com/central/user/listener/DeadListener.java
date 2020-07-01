package com.central.user.listener;

import com.central.common.constant.RabbitMqConstant;
import com.rabbitmq.client.Channel;
import com.central.common.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;

import java.util.Map;

/**
 * 描述:死信消费者
 * @author: myx
 * @date: 2019-05-03
 * Copyright © 2019-grape. All rights reserved.
 */
//@Component
@Slf4j
public class DeadListener {

	@RabbitListener(queues = RabbitMqConstant.DEAD_QUENE_NAME)
	public void process(Message message, @Headers Map<String, Object> headers, Channel channel, UserInfo user) throws Exception {
		log.warn("进入死信队列:"+user.toString()+"，丢失该消息");
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

}