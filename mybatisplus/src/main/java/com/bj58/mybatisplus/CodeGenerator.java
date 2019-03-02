package com.bj58.mybatisplus;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author 牛贞昊（niuzhenhao@58.com）
 * @date 2019/2/22 10:45
 * @desc
 */
// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

        /**
         * <p>
         * 读取控制台内容
         * </p>
         */
        public static String scanner(String tip) {
            Scanner scanner = new Scanner(System.in);
            StringBuilder help = new StringBuilder();
            help.append("请输入" + tip + "：");
            System.out.println(help.toString());
            if (scanner.hasNext()) {
                String ipt = scanner.next();
                if (StringUtils.isNotEmpty(ipt)) {
                    return ipt;
                }
            }
            throw new MybatisPlusException("请输入正确的" + tip + "！");
        }

        public static void main(String[] args) {
            // 代码生成器
            AutoGenerator mpg = new AutoGenerator();

            // 全局配置
            GlobalConfig gc = new GlobalConfig();
            final String projectPath = System.getProperty("user.dir");
            gc.setOutputDir(projectPath + "/mybatisplus/src/main/java");
            gc.setAuthor("justinniu");
            gc.setOpen(false);
            mpg.setGlobalConfig(gc);

            // 数据源配置
            DataSourceConfig dsc = new DataSourceConfig();
            dsc.setUrl("jdbc:mysql://localhost:3306/testdb?useUnicode=true&useSSL=false&characterEncoding=utf8");
            // dsc.setSchemaName("public");
            dsc.setDriverName("com.mysql.jdbc.Driver");
            dsc.setUsername("root");
            dsc.setPassword("root");
            mpg.setDataSource(dsc);

            // 包配置
            final PackageConfig pc = new PackageConfig();
//            pc.setModuleName(scanner("模块名"));
            /**
             *  最外层的包，比如com.bj58.fanglearning
             */
            pc.setParent("com.bj58.mybatisplus");
            mpg.setPackageInfo(pc);


            // 策略配置
            StrategyConfig strategy = new StrategyConfig();
            strategy.setNaming(NamingStrategy.underline_to_camel);
            strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//            strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
            strategy.setEntityLombokModel(false);
            strategy.setRestControllerStyle(false);
//            strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
            strategy.setInclude(scanner("表名"));
            TemplateConfig tc = new TemplateConfig();
            tc.setController(null);
//            strategy.setTablePrefix(pc.getModuleName() + "_");
            mpg.setStrategy(strategy);
            mpg.setTemplate(tc);
//            mpg.setTemplateEngine(new FreemarkerTemplateEngine());
            mpg.execute();
        }

    }
