package com.pyjava.daily.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.pyjava.daily.constants.Constants.*;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/21 16:46
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void initWorkSpace(String workspace) throws IOException {
        logger.debug("the workspace is {}", workspace);
        // 创建folder
        String folderPath = workspace + FILE_SPLIT + NOTE_FOLDER_PATH;
        logger.debug("info file path is {}", folderPath);
        File folder = new File(folderPath);
        if(!folder.exists()){
            boolean newFile = folder.mkdirs();
        }

        // 创建info
        String absoluteInfoFilePath = workspace + FILE_SPLIT + INFO_RELATIVE_PATH;
        logger.debug("info file path is {}", absoluteInfoFilePath);
        File file = new File(absoluteInfoFilePath);
        if(!file.exists()){
            boolean newFile = file.createNewFile();
        }
        // 创建note
        String absoluteNoteFilePath = workspace + FILE_SPLIT + NOTE_RELATIVE_PATH;
        logger.debug("note file path is {}", absoluteNoteFilePath);
        File noteFile = new File(absoluteNoteFilePath);
        if(!noteFile.exists()){
            boolean newFile = noteFile.createNewFile();
        }
    }

    public static boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            // 如果是文件,直接删除
            return dirFile.delete();
        } else {
            // 如果是目录,需要遍历目录下所有文件
            File[] files = dirFile.listFiles();
            if(files != null && files.length > 0){
                for (File file : files) {
                    deleteFile(file);
                }
            }
        }

        return dirFile.delete();
    }

    public static boolean rename(final File file, final String newName) {
        // file is null then return false
        if (file == null) {
            return false;
        }
        // file doesn't exist then return false
        if (!file.exists()) {
            return false;
        }
        // the new name is space then return false
        if (StringUtils.isEmpty(newName)) {
            return false;
        }
        // the new name equals old name then return true
        if (newName.equals(file.getName())) {
            return true;
        }
        File newFile = new File(file.getParent() + File.separator + newName);
        // the new name of file exists then return false
        return !newFile.exists()
                && file.renameTo(newFile);
    }
}
