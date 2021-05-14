package com.pyjava.daily.model;

import java.util.prefs.Preferences;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/14 11:31
 */
public class Config {

    public static void load(Preferences options) {
        lastFilePathProperty().init(options,"lastFilePath",null);
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
        Config.LAST_FILE_PATH.set(lastFilePath);
    }
}
