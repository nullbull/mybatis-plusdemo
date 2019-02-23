package com.bj58.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bj58.mybatisplus.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author justinniu
 * @since 2019-02-22
 */
public interface IUserService extends IService<User> {

     List<User> getUserListByName(String userName) throws Exception;

     List<User> getAll() throws Exception;

     List<User> getUserListByEmail(String email);

     int deleteByMap(Map<String, Object> map);

     int deleteBanchIds(List<Integer> idList);

     IPage getAllByPage(long rows, long offset);

     List<User> getUserByCondition(Map<String, Object> obj);

     List<User> getUser(Object userName, Object email);
}
