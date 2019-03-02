package com.bj58.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.bj58.mybatisplus.test.FenkuInterceptor;
import com.bj58.mybatisplus.test.MyInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 牛贞昊（niuzhenhao@58.com）
 * @date 2019/2/23 11:39
 * @desc
 */
@Configuration
/**
 * 需要加上分页的拦截器
 */
public class config {
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        return paginationInterceptor;
//    }
    @Bean
    public Interceptor fenke() {
        MyInterceptor fenku = new MyInterceptor();
        return fenku;
    }

}
