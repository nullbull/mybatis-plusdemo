package com.bj58.mybatisplus.service.impl;

import com.bj58.mybatisplus.entity.UserTest;
import com.bj58.mybatisplus.mapper.UserTestMapper;
import com.bj58.mybatisplus.service.IUserTestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author justinniu
 * @since 2019-03-02
 */
@Service
public class UserTestServiceImpl extends ServiceImpl<UserTestMapper, UserTest> implements IUserTestService {

}
