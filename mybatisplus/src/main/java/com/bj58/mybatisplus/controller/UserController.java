package com.bj58.mybatisplus.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bj58.mybatisplus.entity.User;
import com.bj58.mybatisplus.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author justinniu
 * @since 2019-02-22
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;
    @PostMapping("/add")
    public String add(User user) {
       return null;
    }
}

