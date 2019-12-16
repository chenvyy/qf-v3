package com.qianfeng.qfv3searchservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.qf.v3.mapper")
public class QfV3SearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QfV3SearchServiceApplication.class, args);
    }

}
