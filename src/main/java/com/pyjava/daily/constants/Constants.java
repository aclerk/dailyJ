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
    public static final String FILE_SPLIT = File.separator;
    public static final String DAILY = ".daily";
    public static final String INFO_FILE_NAME = "info.json";
    public static final String NOTE_PATH = "note";
    public static final String NOTE_INFO_FILE_NAME = "note.json";

    public static final String NOTE_FOLDER_PATH = DAILY + FILE_SPLIT + NOTE_PATH;
    public static final String INFO_RELATIVE_PATH = DAILY + FILE_SPLIT + INFO_FILE_NAME;
    public static final String NOTE_RELATIVE_PATH = DAILY + FILE_SPLIT + NOTE_PATH + FILE_SPLIT + NOTE_INFO_FILE_NAME;
}
