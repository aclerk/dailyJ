package com.pyjava.daily.constants;


import java.io.File;

/**
 * <p>描述: 常量类 </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/10 23:49
 */
public class Constants {
    /**
     * 文件分割
     */
    public static final String FILE_SEPARATOR = File.separator;
    /**
     * 用户目录地址
     */
    public static final String USER_HOME = System.getProperty("user.home");
    /**
     * 全局变量目录名称
     */
    public static final String GLOBAL_CONFIG_FOLDER_NAME = ".daily";
    /**
     * 全局变量目录绝对路径
     */
    public static final String GLOBAL_CONFIG_FOLDER_PATH = USER_HOME + FILE_SEPARATOR + ".daily";
    /**
     * 全局变量文件名称
     */
    public static final String GLOBAL_CONFIG_FILE_NAME = "daily-config.json";
    /**
     * 全局变量文件绝对路径
     */
    public static final String GLOBAL_CONFIG_FILE_PATH = GLOBAL_CONFIG_FOLDER_PATH + FILE_SEPARATOR + "daily-config.json";
}
