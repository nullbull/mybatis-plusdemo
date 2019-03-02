package com.bj58.mybatisplus.test;/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisDefaultParameterHandler;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlParserUtils;
import com.bj58.mybatisplus.ShareTable;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * 分页拦截器
 *
 * @author hubin
 * @since 2016-01-23
 */
@Setter
@Accessors(chain = true)
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class MyInterceptor extends AbstractSqlParserHandler implements Interceptor {

    /**
     * COUNT SQL 解析
     */
    private ISqlParser sqlParser;
    /**
     * 溢出总页数，设置第一页
     */
    private boolean overflow = false;
    /**
     * 方言类型
     */
    private String dialectType;
    /**
     * 方言实现类
     */
    private String dialectClazz;

    private Class clazz;

    private byte INSERT = 1;
    private byte DELETE = 2;
    private byte UPDATE = 3;
    private byte SELECT = 4;
    private byte UNKNOWN = 5;
    private byte sqlCommandType = 5;

    private String method = "getId";

    private int tableCount = 2;



    /**
     * 查询SQL拼接Order By
     *
     * @param originalSql 需要拼接的SQL
     * @param page        page对象
     * @param orderBy     是否需要拼接Order By
     * @return ignore
     */
    public static String concatOrderBy(String originalSql, IPage<?> page, boolean orderBy) {
        if (orderBy && (ArrayUtils.isNotEmpty(page.ascs())
                || ArrayUtils.isNotEmpty(page.descs()))) {
            StringBuilder buildSql = new StringBuilder(originalSql);
            String ascStr = concatOrderBuilder(page.ascs(), " ASC");
            String descStr = concatOrderBuilder(page.descs(), " DESC");
            if (StringUtils.isNotEmpty(ascStr) && StringUtils.isNotEmpty(descStr)) {
                ascStr += ", ";
            }
            if (StringUtils.isNotEmpty(ascStr) || StringUtils.isNotEmpty(descStr)) {
                buildSql.append(" ORDER BY ").append(ascStr).append(descStr);
            }
            return buildSql.toString();
        }
        return originalSql;
    }

    /**
     * 拼接多个排序方法
     *
     * @param columns ignore
     * @param orderWord ignore
     */
    private static String concatOrderBuilder(String[] columns, String orderWord) {
        if (ArrayUtils.isNotEmpty(columns)) {
            return Arrays.stream(columns).filter(StringUtils::isNotEmpty)
                    .map(i -> i + orderWord).collect(joining(StringPool.COMMA)); }
        return StringUtils.EMPTY;
    }


    /**
     * 初始化 判断是否含有@ShareTable注解
     * 如果含有注解 判断sql类型
     * @param metaObject
     * @throws ClassNotFoundException
     */
    private void init(MetaObject metaObject) throws ClassNotFoundException {
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        this.clazz = mappedStatement.getParameterMap().getType();
        String id = mappedStatement.getId();
        String clzName = id.substring(0,id.lastIndexOf("."));
        Class<?> clzObj = Class.forName(clzName);
        ShareTable st = clzObj.getAnnotation(ShareTable.class);
        if (null == st) {
            sqlCommandType = UNKNOWN;
            return;
        }
        method = st.method();
        tableCount = st.tableCount();
        switch (mappedStatement.getSqlCommandType()) {
            case INSERT:
                sqlCommandType = INSERT;
                break;
            case DELETE:
                sqlCommandType = DELETE;
                break;
            case UPDATE:
                sqlCommandType = UPDATE;
                break;
            case SELECT:
                sqlCommandType = SELECT;
                break;
        }

    }

    /**
     * Physical Page Interceptor for all the queries with parameter {@link RowBounds}
     */
    /**
     *  1 增 2 删 3 改 4 查
     * @param invocation
     * @return
     * @throws Throwable
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        /**
         * 判断是否需要分表
         */
        init(metaObject);
        // SQL 解析
        this.sqlParser(metaObject);

        // 针对定义了rowBounds，做为mapper接口方法的参数
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        Object paramObj = boundSql.getParameterObject();
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (sqlCommandType == UNKNOWN) {
            if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
                return invocation.proceed();
            }

            // 判断参数里是否有page对象
            IPage<?> page = null;
            if (paramObj instanceof IPage) {
                page = (IPage<?>) paramObj;
            } else if (paramObj instanceof Map) {
                for (Object arg : ((Map<?,?>) paramObj).values()) {
                    if (arg instanceof IPage) {
                        page = (IPage<?>) arg;
                        break;
                    }
                }
            }

            /*
             * 不需要分页的场合，如果 size 小于 0 返回结果集
             */
            if (null == page || page.getSize() < 0) {
                return invocation.proceed();
            }

            String originalSql = boundSql.getSql();
            Connection connection = (Connection) invocation.getArgs()[0];
            DbType dbType = StringUtils.isNotEmpty(dialectType) ? DbType.getDbType(dialectType)
                    : JdbcUtils.getDbType(connection.getMetaData().getURL());

            boolean orderBy = true;
            if (page.isSearchCount()) {
                SqlInfo sqlInfo = SqlParserUtils.getOptimizeCountSql(page.optimizeCountSql(), sqlParser, originalSql);
                orderBy = sqlInfo.isOrderBy();
                this.queryTotal(overflow, sqlInfo.getSql(), mappedStatement, boundSql, page, connection);
                if (page.getTotal() <= 0) {
                    return invocation.proceed();
                }
            }

            String buildSql = concatOrderBy(originalSql, page, orderBy);
            DialectModel model = DialectFactory.buildPaginationSql(page, buildSql, dbType, dialectClazz);
            Configuration configuration = mappedStatement.getConfiguration();
            List<ParameterMapping> mappings = new ArrayList<>(boundSql.getParameterMappings());
            Map<String, Object> additionalParameters = (Map<String, Object>) metaObject.getValue("delegate.boundSql.additionalParameters");
            model.consumers(mappings, configuration, additionalParameters);
            metaObject.setValue("delegate.boundSql.sql", model.getDialectSql());
            metaObject.setValue("delegate.boundSql.parameterMappings", mappings);
            return invocation.proceed();
        }


        String sql = boundSql.getSql().toLowerCase();
        String[] sqls = sql.split(" ");
        Long id = 0L;
        int pos = 0;
        /**
         *         定位 表名的位置
         */
        switch (sqlCommandType) {
            case 1: {
                for (int i = 0; i < sqls.length; i++) {
                    if ("into".equals(sqls[i])) {
                        pos = i;
                        break;
                    }
                }
                id =  getId(paramObj);
            }
            break;
            case 3: {
                for (int i = 0; i < sqls.length; i++) {
                    if ("update".equals(sqls[i])) {
                        pos = i;
                        break;
                    }
                }
                if (null != clazz) {
                    Method[] methods = clazz.getMethods();
                    Method getUser = null;
                    for (int i = 0; i < methods.length; i++) {
                        if (methods[i].getName().equals("getId")) {
                            getUser = methods[i];
                        }
                    }
                    Map map = (Map) paramObj;
                    Object real = map.get("et");
                    id = (Long) getUser.invoke(real);
                }
            }
            break;
            case 4: {
                for (int i = 0; i < sqls.length; i++) {
                    if ("from".equals(sqls[i])) {
                        pos = i;
                        break;
                    }
                }
                AbstractWrapper wrapper = null;
                if (paramObj instanceof Map) {
                    Map map = (Map) paramObj;
                    wrapper = (AbstractWrapper) map.get("ew");
                }
                if (null != wrapper) {
                    Map<Object, Object> params = wrapper.getParamNameValuePairs();
                    for (Object o : params.keySet()) {
                        if (params.get(o) instanceof Long) {
                            id = (Long) params.get(o);
                            break;
                        }
                    }
                    if (id == 0) {
                        id = (Long) params.get("MPGENVAL1");
                    }
                }
            }
            break;
        }

        /**
         * 表名的位置
         */
        pos++;
        String tableName = sqls[pos];
        System.out.println(tableName);

        /**
         * 分库策略
         */
        if (id > 0) {
            tableName = tableName + "_" + (id % 2);
            sqls[pos] = tableName;
        }
//        System.out.println(tableName);
        /**
         * 重新拼装sql
         */
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sqls.length; i++) {
            sb.append(sqls[i] + " ");
        }
        String originalSql = sb.toString();
        boolean orderBy = true;
        if (sqlCommandType == INSERT || sqlCommandType == UPDATE) {
            metaObject.setValue("delegate.boundSql.sql", originalSql);
            return invocation.proceed();
        }
        if (sqlCommandType == SELECT ) {
            DialectModel model = null;
            // 判断参数里是否有page对象
            IPage<?> page = null;
            if (paramObj instanceof IPage) {
                page = (IPage<?>) paramObj;
            } else if (paramObj instanceof Map) {
                for (Object arg : ((Map<?,?>) paramObj).values()) {
                    if (arg instanceof IPage) {
                        page = (IPage<?>) arg;
                        break;
                    }
                }
            }

            /*
             * 不需要分页的场合，如果 size 小于 0 返回结果集
             */
            if (null == page || page.getSize() < 0) {
                List<ParameterMapping> mappings = new ArrayList<>(boundSql.getParameterMappings());
                metaObject.setValue("delegate.boundSql.sql", originalSql);
                metaObject.setValue("delegate.boundSql.parameterMappings", mappings);
                return invocation.proceed();
            }
            /**
             * 分页策略
             */
            Connection connection = (Connection) invocation.getArgs()[0];
            DbType dbType = StringUtils.isNotEmpty(dialectType) ? DbType.getDbType(dialectType)
                    : JdbcUtils.getDbType(connection.getMetaData().getURL());
            if (page.isSearchCount()) {
                SqlInfo sqlInfo = SqlParserUtils.getOptimizeCountSql(page.optimizeCountSql(), sqlParser, originalSql);
                orderBy = sqlInfo.isOrderBy();
                this.queryTotal(overflow, sqlInfo.getSql(), mappedStatement, boundSql, page, connection);
            }
            String buildSql = concatOrderBy(originalSql, page, orderBy);
            model = DialectFactory.buildPaginationSql(page, buildSql, dbType, dialectClazz);
            Configuration configuration = mappedStatement.getConfiguration();
            List<ParameterMapping> mappings = new ArrayList<>(boundSql.getParameterMappings());
            Map<String, Object> additionalParameters = (Map<String, Object>) metaObject.getValue("delegate.boundSql.additionalParameters");
            model.consumers(mappings, configuration, additionalParameters);
            metaObject.setValue("delegate.boundSql.sql", model.getDialectSql());
            metaObject.setValue("delegate.boundSql.parameterMappings", mappings);
        }

        return invocation.proceed();
    }


    private Long getId(Object o) throws InvocationTargetException, IllegalAccessException {
        Long id = 0L;
        if (null != clazz) {
            Method[] methods = clazz.getMethods();
            Method getUser = null;
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals(method)) {
                    getUser = methods[i];
                }
            }
            id =  (Long) getUser.invoke(o);
        }
        return id;
    }

    /**
     * 查询总记录条数
     *
     * @param sql             count sql
     * @param mappedStatement MappedStatement
     * @param boundSql        BoundSql
     * @param page            IPage
     * @param connection      Connection
     */
    protected void queryTotal(boolean overflowCurrent, String sql, MappedStatement mappedStatement, BoundSql boundSql, IPage<?> page, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            DefaultParameterHandler parameterHandler = new MybatisDefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), boundSql);
            parameterHandler.setParameters(statement);
            long total = 0;
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    total = resultSet.getLong(1);
                }
            }
            page.setTotal(total);
            /*
             * 溢出总页数，设置第一页
             */
            long pages = page.getPages();
            if (overflowCurrent && page.getCurrent() > pages) {
                // 设置为第一条
                page.setCurrent(1);
            }
        } catch (Exception e) {
            throw ExceptionUtils.mpe("Error: Method queryTotal execution error of sql : \n %s \n", e, sql);
        }
    }


    protected void insertTest(boolean overflowCurrent, String sql, MappedStatement mappedStatement, BoundSql boundSql, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            DefaultParameterHandler parameterHandler = new MybatisDefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), boundSql);
            parameterHandler.setParameters(statement);
            long total = 0;
            statement.execute();
            /*
             * 溢出总页数，设置第一页
             */
        } catch (Exception e) {
            throw ExceptionUtils.mpe("Error: Method queryTotal execution error of sql : \n %s \n", e, sql);
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties prop) {
        String dialectType = prop.getProperty("dialectType");
        String dialectClazz = prop.getProperty("dialectClazz");
        if (StringUtils.isNotEmpty(dialectType)) {
            this.dialectType = dialectType;
        }
        if (StringUtils.isNotEmpty(dialectClazz)) {
            this.dialectClazz = dialectClazz;
        }
    }
}
