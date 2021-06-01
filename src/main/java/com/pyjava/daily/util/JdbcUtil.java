package com.pyjava.daily.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * <p>描述: jdbc工具 </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/30 23:47
 */
public class JdbcUtil {
    private static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL_PREFIX = "jdbc:sqlite:";

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

    /**
     * 获取连接
     */
    private static Connection getConnection(String dbName) {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + dbName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
