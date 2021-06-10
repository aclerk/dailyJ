package com.pyjava.daily.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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

    public static void initDb(String dbName) throws Exception {
        Connection connection = getConnection(dbName);
        if (connection != null) {
            StringBuffer command = null;
            URL resource = JdbcUtil.class.getClassLoader().getResource("sql/init.sql");
            assert resource != null;
            String sqlPath = resource.getPath();
            logger.debug(sqlPath);
            FileReader fileReader = new FileReader(new File(sqlPath));
            try {
                LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
                String line;
                while ((line = lineNumberReader.readLine()) != null) {
                    if (command == null) {
                        command = new StringBuffer();
                    }
                    String trimmedLine = line.trim();
                    if (trimmedLine.endsWith(";")) {
                        command.append(line, 0, line.lastIndexOf(";"));
                        command.append(" \n");
                        Statement statement = connection.createStatement();
                        String cmd = command.toString();
                        logger.debug(cmd);
                        statement.execute(cmd);
                        command = null;
                        try {
                            statement.close();
                        } catch (Exception e) {
                            // ignore
                        }
                    } else if (!trimmedLine.startsWith("--") && !trimmedLine.startsWith("//")) {
                        command.append(line);
                        command.append(" \n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception();
            }
        }
    }

    /**
     * 获取连接
     *
     * @param dbName 数据库文件路径
     * @return 连接 {@link Connection}
     */
    public static Connection getConnection(String dbName) {
        logger.debug("db name is " + dbName);
        try {
            return DriverManager.getConnection(URL_PREFIX + dbName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        initDb("C:\\Users\\dell\\Desktop\\test.db");
    }
}
