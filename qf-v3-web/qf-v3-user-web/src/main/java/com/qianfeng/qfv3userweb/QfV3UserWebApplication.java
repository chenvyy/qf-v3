package com.qianfeng.qfv3userweb;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDubbo
public class QfV3UserWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(QfV3UserWebApplication.class, args);
    }

}
