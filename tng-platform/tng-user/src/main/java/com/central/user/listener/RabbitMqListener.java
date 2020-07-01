package com.central.user.listener;

import com.central.common.constant.RabbitMqConstant;
import com.central.common.exception.IdempotencyException;
import com.central.common.model.Result;
import com.rabbitmq.client.Channel;
import com.central.common.model.UserInfo;
import com.central.user.service.IUserInfoService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.Map;

//@Component
public class RabbitMqListener {
    @Autowired
    private IUserInfoService userInfoService;

    @RabbitListener(queues = RabbitMqConstant.QUEUE_SAVE_NAME,containerFactory = "customContainerFactory")
    @SendTo(RabbitMqConstant.QUEUE_SAVE_NAME)
    public Result process(Message message, @Headers Map<String, Object> headers, Channel channel, UserInfo user) throws Exception {
        Result result = null;
        try {
            result = userInfoService.saveOrUpdateUser(user);
            String msg = new String(message.getBody(), "UTF-8");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IdempotencyException e) {
            e.printStackTrace();
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            return Result.failed("该用户名已存在");
        } catch (Exception e) {
            e.printStackTrace();
            // // 丢弃该消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            return  Result.failed("保存失败");
        }
        return Result.succeed("保存成功");
    }
}