package com.bj58.mybatisgeneration.service.impl;

import com.bj58.mybatisgeneration.dao.UserMapper;
import com.bj58.mybatisgeneration.entity.User;
import com.bj58.mybatisgeneration.service.IUserService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.executor.statement.SimpleStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.reflection.ParamNameUtil;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

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

    @Autowired
    UserMapper userMapper;

    @Autowired
    SqlSession sqlSession;

    @Test
    public void selectByPage() {
        PageInfo<User> pageInfo = new PageInfo<>();
        pageInfo = userService.selectByPage(1, 2);
        pageInfo.getList().stream().forEach(System.out::println);
    }
    @Test
    public void selectById() {
        userMapper.selectById(1, "zwt");
    }

    @Test
    public void testSqlSource() throws Exception {

        SqlSession session = sqlSession;
        Configuration configuration = session.getConfiguration();
        MappedStatement mappedStatement = configuration
                .getMappedStatement("com.bj58.mybatisgeneration.dao.UserMapper.selectById");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 1);
        map.put("name", "zwt");
        BoundSql boundSql2 = mappedStatement.getBoundSql(map);
        String sql2 = boundSql2.getSql();
        System.out.println(sql2);
        User user1 = session.selectOne("com.bj58.mybatisgeneration.dao.UserMapper.selectById", map);
        System.out.println(user1);

        User user = new User();
        user.setUsername("justinniu");
        user.setPassword("123456");
        user.setId(122L);
        MappedStatement insertStatement = configuration.getMappedStatement("com.bj58.mybatisgeneration.dao.UserMapper.insert");
        insertStatement.getBoundSql(user);
        SqlSource sqlSource = insertStatement.getSqlSource();
        BoundSql boundSql3 =  sqlSource.getBoundSql(user);
        System.out.println(boundSql3.getSql());
        int result = session.insert("com.bj58.mybatisgeneration.dao.UserMapper.insert", user);

        UserMapper userMapper = session.getMapper(UserMapper.class);
        Method method = userMapper.getClass().getMethod("insert", User.class);
        ParamNameResolver paramNameResolver = new ParamNameResolver(configuration, method);
        Object object = paramNameResolver.getNamedParams(new User[]{user});
        ParamNameUtil.getParamNames(method).stream().forEach(System.out::println);
    }

    @Test
    public void shouldConditionallyChooseFirst() throws Exception {
        DynamicSqlSource source = createDynamicSqlSource(
                new TextSqlNode("SELECT * FROM user where id = ${id} and username = #{name}"));
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", 1);
        hashMap.put("name", "zwt");
        BoundSql boundSql = source.getBoundSql(hashMap);
        System.out.println(boundSql.getSql());
    }

    private DynamicSqlSource createDynamicSqlSource(SqlNode... contents) throws IOException, SQLException {
        Configuration configuration = sqlSession.getConfiguration();
        MixedSqlNode sqlNode = mixedContents(contents);
        return new DynamicSqlSource(configuration, sqlNode);
    }
    private MixedSqlNode mixedContents(SqlNode... contents) {
        return new MixedSqlNode(Arrays.asList(contents));
    }

    @Test
    public void stateMentHandler() {
    }

}
