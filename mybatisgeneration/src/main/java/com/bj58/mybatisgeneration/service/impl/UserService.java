package com.bj58.mybatisgeneration.service.impl;

import com.bj58.mybatisgeneration.dao.UserMapper;
import com.bj58.mybatisgeneration.entity.User;
import com.bj58.mybatisgeneration.entity.UserExample;
import com.bj58.mybatisgeneration.service.IUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 牛贞昊（niuzhenhao@58.com）
 * @date 2019/2/23 15:56
 * @desc
 */
@Service
public class UserService implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public int insert(User user) {
        return userMapper.insert(user);
    }

    /**
     * 虽然看起来 功能都差不多，但是代码量多了很多
     * @param userName
     * @return
     */

    @Override
    public List<User> selectByUserName(String userName) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdBetween(1L, 5L);
        return userMapper.selectByExample(userExample);
    }

    @Override
    public List<User> selectBatchIds(List<Long> idList) {
        List<User> users = new ArrayList<>();
        for (long id : idList) {
            users.add(userMapper.selectByPrimaryKey(id));
        }
        return users;
    }

    @Override
    public int deleletByMap(Map<String, String> map) {
        String userAge  = map.get("age");
        String email = map.get("email");
        String phone = map.get("phone");
        UserExample example = new UserExample();

        return userMapper.deleteByExample(example);
    }

    @Override
    public PageInfo<User> selectByPage(int row, int offset) {
        return null;
    }

//    @Override
//    public PageInfo<User> selectByPage(int row, int offset) {
//        PageHelper.startPage(row, offset);
//    }
}
