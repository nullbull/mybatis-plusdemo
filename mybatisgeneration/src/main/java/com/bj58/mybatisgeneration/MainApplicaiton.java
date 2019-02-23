package com.bj58.mybatisgeneration;

import org.apache.catalina.core.ApplicationContext;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author 牛贞昊（niuzhenhao@58.com）
 * @date 2019/2/23 12:37
 * @desc
 */
@SpringBootApplication
@MapperScan("com.bj58.mybatisgeneration.dao")
public class MainApplicaiton {
    public static void main(String[] args) {
        SpringApplication.run(MainApplicaiton.class, args);
    }
}
