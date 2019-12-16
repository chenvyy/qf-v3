package com.qianfeng.qfv3searchservice.config;

import com.qf.v3.common.constant.RabbitMqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author CYY
 * @date 2019/10/18 0018 17:19
 */
@Configuration
public class RabbitMqConfig {
    //声明队列
    @Bean
    public Queue getQueue(){
        return new Queue(RabbitMqConstant.PRODUCT_ADD_QUEUE,true,false,false);
    }
    //声明交换机
    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange(RabbitMqConstant.PRODUCT_EXCHANGE);
    }
    //绑定队列和交换机关系
    @Bean
    public Binding getBinding(Queue getQueue,TopicExchange getTopicExchange){
        return BindingBuilder.bind(getQueue).to(getTopicExchange).with("product.add");
    }
}
