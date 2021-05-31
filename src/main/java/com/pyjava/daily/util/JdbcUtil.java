package com.pyjava.daily.util;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/30 23:47
 */
public class JdbcUtil {
    private static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL_PREFIX = "jdbc:sqlite:";
    private static Connection connection = null;
    private static SqlSessionFactory sqlSessionFactory = null;

    static {
        try {
            // 加载数据库驱动
            logger.info("start loading org.sqlite.JDBC");
            Class.forName(DRIVER);
            logger.info("finish loading org.sqlite.JDBC");
        } catch (Exception e) {
            logger.error("error {} in loading org.sqlite.JDBC", e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void initConnection(String url) throws Exception {
        if (connection == null) {
            try {
                DataSource dataSource = new UnpooledDataSource("org.sqlite.JDBC", URL_PREFIX + url, "", "");
                // 初始化
                Configuration configuration = new Configuration();
                configuration.setMapUnderscoreToCamelCase(true);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
                // 初始化数据库
                connection = dataSource.getConnection();
                ScriptRunner runner = new ScriptRunner(connection);
                runner.setErrorLogWriter(null);
                runner.setLogWriter(null);
                runner.runScript(Resources.getResourceAsReader("sql/init.sql"));
            } catch (SQLException throwables) {
                logger.error("error {} in get connection", throwables.getMessage());
                throw new Exception(throwables);
            }
        }
    }
}
