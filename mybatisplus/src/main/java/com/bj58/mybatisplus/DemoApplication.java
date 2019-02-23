package com.bj58.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 牛贞昊（niuzhenhao@58.com）
 * @date 2019/2/22 15:09
 * @desc
 */
@SpringBootApplication
@MapperScan("com.bj58.mybatisplus.mapper")
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
