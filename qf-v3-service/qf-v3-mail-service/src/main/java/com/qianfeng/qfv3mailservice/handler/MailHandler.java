package com.qianfeng.qfv3mailservice.handler;

import com.qf.v3.common.constant.RabbitMqConstant;
import com.qianfeng.qfv3mailservice.service.MailServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author CYY
 * @date 2019/10/22 0022 17:52
 */
@Component
public class MailHandler {

    @Resource
    private MailServiceImpl mailService;

    @RabbitListener(queues= RabbitMqConstant.MAIL_QUEUE)
    public void mailReceiver(String to){
        System.out.println(to);
        //收到消息 发邮件
        mailService.sendActiveMail("添加成功");
    }
}
