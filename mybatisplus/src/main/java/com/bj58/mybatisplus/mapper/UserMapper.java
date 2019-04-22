package com.bj58.mybatisplus.mapper;

import com.bj58.mybatisplus.ShareTable;
import com.bj58.mybatisplus.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author justinniu
 * @since 2019-02-22
 */
//@ShareTable(method = "getId", tableCount = 2)
public interface UserMapper extends BaseMapper<User> {
    User selectByID(@Param("id") Long id);
}
