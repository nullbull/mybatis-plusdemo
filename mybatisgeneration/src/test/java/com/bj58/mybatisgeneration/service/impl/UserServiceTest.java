package com.bj58.mybatisgeneration.service.impl;

import com.bj58.mybatisgeneration.entity.User;
import com.bj58.mybatisgeneration.service.IUserService;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author 牛贞昊（niuzhenhao@58.com）
 * @date 2019/2/23 16:03
 * @desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    IUserService userService;
    @Test
    public void insert() {
        User user = new User();
        user.setName("justinniu");
        user.setAge(23);
        user.setId(8L);
        user.setEmail("1129114837@qq.com");
        userService.insert(user);
    }

    @Test
    public void insertBunch() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("justinniu" + i);
            user.setAge(23 + i);
            user.setId(9L + i);
            user.setEmail("1129114837@qq.com + i" + i);
            users.add(user);
        }
        userService.insertBunch(users);
    }

    @Test
    public void selectByPage() {
        PageInfo<User> pageInfo = new PageInfo<>();
        pageInfo = userService.selectByPage(1, 2);
        pageInfo.getList().stream().forEach(System.out::println);
    }
}
