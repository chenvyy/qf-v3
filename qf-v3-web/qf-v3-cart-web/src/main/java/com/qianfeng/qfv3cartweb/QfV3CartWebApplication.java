package com.qianfeng.qfv3cartweb;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDubbo
public class QfV3CartWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(QfV3CartWebApplication.class, args);
    }

}
