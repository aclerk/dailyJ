package com.pyjava.daily.config;

import com.pyjava.daily.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/14 11:31
 */
public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    public static void load(Preferences options) throws Exception {
        lastFilePathProperty().init(options,"lastFilePath",null);
        String lastFilePath = Config.getLastFilePath();
        if(null != lastFilePath ){
            String db = lastFilePath+"\\.daily\\daily.db";
            if(new File(db).exists()){
                JdbcUtil.getConnection(db);
            }
        }else{
            Config.setLastFilePath("");
        }
    }

    /**
     * 当前打开位置属性
     */
    private static final ConfigStringProperty NOW_FILE_PATH =new ConfigStringProperty();

    public static String getNowFilePath() {
        return NOW_FILE_PATH.get();
    }

    public static ConfigStringProperty nowFilePathProperty() {
        return NOW_FILE_PATH;
    }

    public static void setNowFilePath(String nowFilePath) {
        logger.debug("set now file path:{}", nowFilePath);
        Config.NOW_FILE_PATH.set(nowFilePath);
    }

    /**
     * 上一次文件打开位置属性
     */
    private static final ConfigStringProperty LAST_FILE_PATH =new ConfigStringProperty();
    public static String getLastFilePath() {
        return LAST_FILE_PATH.get();
    }
    public static ConfigStringProperty lastFilePathProperty() {
        return LAST_FILE_PATH;
    }
    public static void setLastFilePath(String lastFilePath) {
        logger.debug("set last file path:{}", lastFilePath);
        Config.LAST_FILE_PATH.set(lastFilePath);
    }
}
