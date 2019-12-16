package com.qianfeng.qfv3mailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class QfV3MailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QfV3MailServiceApplication.class, args);
    }

}
