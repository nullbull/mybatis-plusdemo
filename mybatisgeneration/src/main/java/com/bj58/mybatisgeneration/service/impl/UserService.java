package com.bj58.mybatisgeneration.service.impl;

import com.bj58.mybatisgeneration.dao.UserMapper;
import com.bj58.mybatisgeneration.entity.User;
import com.bj58.mybatisgeneration.entity.UserExample;
import com.bj58.mybatisgeneration.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
                .andNameEqualTo("userName")
                .andIdBetween(1L, 5L);
        return userMapper.selectByExample(userExample);
    }

    @Override
    public int insertBunch(List<User> usersList) {
        usersList.stream().forEach(userMapper::insert);
        return 1;
    }

    @Override
    public int deleletByMap(Map<String, String> map) {
        return 0;
    }
}
