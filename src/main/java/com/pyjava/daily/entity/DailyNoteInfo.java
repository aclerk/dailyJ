package com.pyjava.daily.entity;

import java.util.Date;
import java.util.List;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/27 22:54
 */
public class DailyNoteInfo {
    private String id;
    private String version;
    private Date createdTime;
    private Date modifiedTime;
    private List<FileInfo> fileInfos;
    private List<FolderInfo> folderInfos;
}
