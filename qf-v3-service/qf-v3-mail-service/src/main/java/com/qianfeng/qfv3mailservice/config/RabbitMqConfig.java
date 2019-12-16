package com.qianfeng.qfv3mailservice.config;

import com.qf.v3.common.constant.RabbitMqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author CYY
 * @date 2019/10/22 0022 17:45
 */
@Configuration
public class RabbitMqConfig {
    //声明队列,参数1：队列名
    //        参数2-4：是否持久化，是否私有化，是否自动删除
    @Bean
    public Queue getQueue(){
        return new Queue(RabbitMqConstant.MAIL_QUEUE,true,false,false);
    }

    //声明交换机
    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange(RabbitMqConstant.MAIL_EXCHANGE);
    }

    //绑定队列和交换机的关系
    @Bean
    public Binding getBinding(Queue getQueue,TopicExchange getTopicExchange){
        return BindingBuilder.bind(getQueue).to(getTopicExchange).with("mail.send");
    }
}
