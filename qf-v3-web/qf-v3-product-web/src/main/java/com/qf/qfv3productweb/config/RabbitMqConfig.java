package com.qf.qfv3productweb.config;

import com.qf.v3.common.constant.RabbitMqConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author CYY
 * @date 2019/10/18 0018 17:13
 */
@Configuration
public class RabbitMqConfig {
    //声明一个交换机
    @Bean
    public TopicExchange getProductTopicExchange(){
        return new TopicExchange(RabbitMqConstant.PRODUCT_EXCHANGE);
    }

    //声明交换机
    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange(RabbitMqConstant.MAIL_EXCHANGE);
    }
}
