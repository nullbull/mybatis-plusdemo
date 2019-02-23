package com.bj58.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bj58.mybatisplus.entity.User;
import com.bj58.mybatisplus.service.IUserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;


/**
 * @author 牛贞昊（niuzhenhao@58.com）
 * @date 2019/2/22 14:58
 * @desc
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {
    @Autowired
    IUserService userService;

    @Test
    public void getByName() throws Exception {
        Assert.assertTrue(userService.getUserListByName("zwt").size() > 0);
    }

    @Test
    public void getAll() throws Exception {
        Assert.assertTrue(userService.getAll().size() == 5);
    }

    @Test
    public void deleteByMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "zwt");
        map.put("id", "1");
        Assert.assertTrue(userService.deleteByMap(map) == 1);
    }

    @Test
    public void getByPage() {
        System.out.println("筛选第一页");
        Page<User> userPage = (Page<User>) userService.getAllByPage(0, 2);
        System.out.println(userPage.getCurrent());
        System.out.println(userPage.getTotal());
        userPage.getRecords().forEach(System.out::println);
        System.out.println("筛选第二页");
        userService.getAllByPage(2, 1).getRecords().stream().forEach(System.out::println);
    }

    @Test
    public void getByCondition() {
        userService.getUser("zwt", "2").stream().forEach(System.out::println);
    }
}
