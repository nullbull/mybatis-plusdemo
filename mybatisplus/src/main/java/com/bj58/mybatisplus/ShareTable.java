package com.bj58.mybatisplus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 牛贞昊（niuzhenhao@58.com）
 * @date 2019/3/2 13:10
 * @desc
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ShareTable {
    String method() default "getId";
    int tableCount() default 2;
}
