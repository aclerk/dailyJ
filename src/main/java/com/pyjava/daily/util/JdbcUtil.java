package com.pyjava.daily.util;


import com.pyjava.daily.mapper.NotebookMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
    private static SQLiteDataSource dataSource;

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

    public static void init(String dbName) {
        logger.debug("initializing db:{}", dbName);
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(URL_PREFIX + dbName);
        dataSource = ds;
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("default", transactionFactory, dataSource);
        configuration.setEnvironment(environment);
        configuration.addMapper(NotebookMapper.class);
        return new SqlSessionFactoryBuilder().build(configuration);
    }

    public static SQLiteDataSource getDataSource() {
        return dataSource;
    }

    public static void initDb(String dbName) throws Exception {
        logger.debug("initializing db:{}", dbName);
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(URL_PREFIX + dbName);
        Connection connection = ds.getConnection();
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
            } finally {
                close(connection);
            }
        }
    }

    /**
     * 获取连接
     *
     * @return 连接 {@link Connection}
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
