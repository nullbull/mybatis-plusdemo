package com.bj58.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.Condition;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bj58.mybatisplus.entity.User;
import com.bj58.mybatisplus.mapper.UserMapper;
import com.bj58.mybatisplus.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author justinniu
 * @since 2019-02-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    UserMapper userMapper;

    /**
     *         userMapper.selectList(new QueryWrapper<User>().lambda().eq(User::getUsername, userName))
     *         会报错， 需要加上User
     */
    @Override
    public List<User> getUserListByName(String userName) throws Exception {
        User user = new User();
        userMapper.insert(user);

        return userMapper.selectList(new QueryWrapper<User>().lambda().eq(User::getName, userName));
    }

    /**
     * 获取所有的User
     * @return
     */
    @Override
    public List<User> getAll() throws Exception{
        return userMapper.selectList(null);
    }

    @Override
    public List<User> getUserListByEmail(String email) {
        return null;
    }

    /**
     *
     * @param map 根据map 的参数删除 满足条件的列
     * @return
     */
    @Override
    public int deleteByMap(Map<String, Object> map) {
        return userMapper.deleteByMap(map);
    }

    /**
     *
     * @param idList 根据idList 批量删除
     * @return
     */
    @Override
    public int deleteBanchIds(List<Integer> idList) {
        return userMapper.deleteBatchIds(idList);
    }

    /**
     * 不加分页拦截器就是内存分页，即把所有满足条件的列查询出来，然后在分页
     * 物理分页就是在Sql 加上了 offset，
     * @param rows
     * @param offset
     * @return
     */
    @Override
    public IPage getAllByPage(long rows, long offset) {
        return userMapper.selectPage(new Page<User>(rows, offset), null);
    }

    @Override
    public List<User> getUserByCondition(Map<String, Object> obj) {
        return userMapper.selectByMap(obj);
    }

    @Override
    public List<User> getUser(Object userName, Object email) {
        return userMapper.selectList(new QueryWrapper<User>()
                .eq("email", email)
                .eq("name", userName)
                .lt("id", 3));
    }

    @Override
    public int selectCount() {
        Date startDate = new Date();
        Date currentDate = new Date();
        int buyCount = userMapper.selectCount(new QueryWrapper<User>()
                .select("sum(quantity)")
                .isNull("order_id")
                .eq("user_id", 1)
                .eq("type", 1)
                .in("status", new Integer[]{0, 1})
                .eq("product_id", 1)
                .between("created_time", startDate, currentDate)
                .eq("weal", 1));
        return buyCount;
    }

    @Override
    public List<User> getUserByLambda(String age, String email) {
        return userMapper.selectList(new QueryWrapper<User>().lambda()
        .eq(User::getAge, Integer.valueOf(age))
        .eq(User::getEmail, email)
        );
    }

    @Override
    public List<User> getUserByLambdaAnother(Integer age, String email) {
        return userMapper.selectList(new QueryWrapper<User>().lambda()
                .and(obj -> obj.eq(User::getEmail, email)
                .eq(User::getAge, age)));
    }


    @Override
    public List<User> getUserByIdList(List<Long> idList) {
        return userMapper.selectBatchIds(idList);
    }


}
