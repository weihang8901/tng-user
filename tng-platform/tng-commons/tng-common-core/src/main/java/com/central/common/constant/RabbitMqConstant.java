package com.central.common.constant;
/**
 * rabbitmq常量
 *
 * @author wh
 * @date 2020/4/24
 */
public interface RabbitMqConstant {
    /**
     * 队列名称
     */
    public static final String QUEUE_SAVE_NAME = "save_queue_api";

    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME = "fanout";


    /**
     *  定义死信队列相关信息
     */
    public final static String DEAD_QUENE_NAME = "dead_queue";
    public final static String DEAD_ROUTING_KEY = "dead_routing_key";
    public final static String DEAD_EXCHANGE_NAME = "dead_exchange";
    /**
     * 死信队列 交换机标识符
     */
    public static final String DEAD_LETTER_EXCHANGE_KEY = "x-dead-letter-exchange";
    /**
     * 死信队列交换机绑定键标识符
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
}
