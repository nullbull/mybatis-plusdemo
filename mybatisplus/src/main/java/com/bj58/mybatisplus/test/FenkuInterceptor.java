package com.bj58.mybatisplus.test;

import com.bj58.mybatisplus.entity.User;
import net.sf.jsqlparser.schema.Table;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_FACTORY;
import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY;

/**
 * @author 牛贞昊（niuzhenhao@58.com）
 * @date 2019/2/28 20:04
 * @desc
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class FenkuInterceptor implements Interceptor {

    //SQL解析工厂

    //sql语句存储字段
    private final Field boundSqlField;

    private static JSqlparserFactory jSqlparserFactory = new JSqlparserFactory();
    public FenkuInterceptor() {
        try {
            boundSqlField = BoundSql.class.getDeclaredField("sql");
            boundSqlField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        if (invocation.getTarget() instanceof Executor) {
            return invocation.proceed();
        }

        System.out.println("进入拦截器：====================");

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        MetaObject mo = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) mo.getValue("delegate.mappedStatement");

        //解析出MappedStatement的ID 从中获取Dao类信息
        String id = mappedStatement.getId();
        String clzName = id.substring(0,id.lastIndexOf("."));
        Class<?> clzObj = Class.forName(clzName);
        //是否添加 @TableShard注解


            // 进行SQL解析，如果未找到表名，则跳过
            BoundSql boundSql = statementHandler.getBoundSql();
            SqlParser sqlParser = jSqlparserFactory.createParser(boundSql.getSql());
            List<Table> tables = sqlParser.getTables();
            if (tables.isEmpty()) {
                return invocation.proceed();
            }

            //获取分表后缀名
            Long baseId = null;
            Object v2 = mo.getValue("delegate.boundSql.parameterObject");
            if (v2 instanceof Map){
                Map pm = (Map) v2;
                //一定先从参数中查询，是否有 @Param("shardName") 的参数， 如果有，当做分表后缀，
                // 如果没有， 将遍历参数， 找到实现了ShardEntity接口的参数
                if (baseId == null){
                    Collection values = pm.values();
                    for (Object o : values) {
                        if (o instanceof User){
                            User entity = (User) o;
                            baseId = entity.getId();
                            break;
                        }
                    }
                }
                //如果只有一个参数，为实体类，则直接从中获取属性
            }else {
                if (v2 instanceof User) {
                    User se = (User) v2;
                    baseId = se.getId();
                }
            }
             System.out.println(baseId);
            //如果参数中 未包含 shardName 相关参数， 则抛出异常
            if (baseId == null)
                throw new Exception("shardName must be not empty!");
            // 设置实际的表名
            for (int index = 0; index < tables.size(); index++) {
                Table table = tables.get(index);
                //替换所有表名，为表名添加后缀
                String targetName = table.getName() + "_" + baseId % 2;
                table.setName(targetName);
            }

            // 修改实际的SQL
            String targetSQL = sqlParser.toSQL();
            boundSqlField.set(boundSql, targetSQL);
            return invocation.proceed();
        }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
