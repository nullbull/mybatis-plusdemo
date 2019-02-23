package com.bj58.mybatisgeneration.service;

import com.bj58.mybatisgeneration.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author 牛贞昊（niuzhenhao@58.com）
 * @date 2019/2/23 15:56
 * @desc
 */
public interface IUserService {

    int insert(User user);

    List<User> selectByUserName(String userName);

    int insertBunch(List<User> usersList);


    int deleletByMap(Map<String, String> map);

}
