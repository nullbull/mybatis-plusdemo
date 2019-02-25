package com.bj58.mybatisgeneration.service;

import com.bj58.mybatisgeneration.entity.User;
import com.github.pagehelper.PageInfo;

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

    List<User> selectBatchIds(List<Long> ids);

    int deleletByMap(Map<String, String> map);

    PageInfo<User> selectByPage(int row, int offset);

}
