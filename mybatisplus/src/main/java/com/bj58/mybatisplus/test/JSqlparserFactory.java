package com.bj58.mybatisplus.test;

/**
 * @author 牛贞昊（niuzhenhao@58.com）
 * @date 2019/2/28 20:32
 * @desc
 */
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;

import java.io.StringReader;
import java.sql.SQLException;

public class JSqlparserFactory {
    private final CCJSqlParserManager manager;

    public JSqlparserFactory() {
        manager = new CCJSqlParserManager();
    }

    public SqlParser createParser(String originalSql) throws SQLException {
        try {
            Statement statement = manager.parse(new StringReader(originalSql));
            SqlParser parse = new SqlParser(statement);
            parse.init();
            return parse;
        } catch (JSQLParserException e) {
            throw new SQLException("SQL Parse Failed", e);
        }

    }
}
