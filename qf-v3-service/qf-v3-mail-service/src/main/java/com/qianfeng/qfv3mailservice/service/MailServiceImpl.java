package com.qianfeng.qfv3mailservice.service;

import com.qf.v3.mail.api.IMailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author CYY
 * @date 2019/10/22 0022 17:39
 */
@Component
public class MailServiceImpl implements IMailService {
    /**
     * 注入邮件发送者对象
     */
    @Resource
    private JavaMailSender mailSender;

    /**
     * 注入发送邮件需要的模板引擎对象
     */
    @Resource
    private TemplateEngine templateEngine;

    @Value("${From.path}")
    private String From;

    @Value("${To.path}")
    private String To;

    @Override
    public void sendActiveMail(String to) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            //模板+数据=输出
            //数据
            Context ctx = new Context();
            ctx.setVariable("username","商城");
            //模板
            String info = templateEngine.process("active", ctx);
            mimeMessageHelper.setText(info, true);
            mimeMessageHelper.setSubject("添加成功");
            mimeMessageHelper.setFrom(From);
            mimeMessageHelper.setTo(To);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            //出现异常是正常情况
            //收件人地址有问题
            //邮件服务器出现问题
            //敏感词汇
            //网络问题
            //发生异常一般会给一个错误码
            //邮件服务器厂商会有错误列表

            //补救:
            //需要将失败的信息存储到db中
            //定时任务-->定时轮训发送邮件失败表进行重新发送
            //定时任务?
            //指在规定的具体的时间点去做一件事情
            //周期定的做某一件事情
            //jdk提供定时任务的实现 timer jdk自带
        }
    }
}
