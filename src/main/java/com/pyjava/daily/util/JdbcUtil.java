package com.pyjava.daily.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/14 1:14
 */
public class JdbcUtil {
    private static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL_PREFIX = "jdbc:sqlite:";
    private static Connection connection = null;

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

    public static Connection getConnection(String url) throws Exception {
        if(connection == null){
            try {
                connection= DriverManager.getConnection(URL_PREFIX + url, "", "");
            } catch (SQLException throwables) {
                logger.error("error {} in get connection", throwables.getMessage());
                throw new Exception(throwables);
            }
        }
        logger.info("get connection {}, status: {}", url, connection!=null?"success":"false");
        return connection;
    }

    public static void initDatabase() throws SQLException {
        if(connection == null){
            logger.error("connection is null");
            return;
        }
        connection.setAutoCommit(false);
        Statement stmt = connection.createStatement();
        boolean execute = stmt.execute("create table `daily_option`\n" +
                "(\n" +
                "    `option_id` bigint primary key not null ,\n" +
                "    `option_name` varchar(256) not null ,\n" +
                "    `option_desc` varchar(256),\n" +
                "    `param_name` varchar(256) not null ,\n" +
                "    `param_desc` varchar(256),\n" +
                "    `value` varchar(256),\n" +
                "    `value_desc` varchar(256),\n" +
                "    `create_date` datetime\n" +
                ")");
        logger.info("创建表option{}",execute);
    }
}
