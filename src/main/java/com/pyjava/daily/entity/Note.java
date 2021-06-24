package com.pyjava.daily.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/22 23:53
 */
@Data
public class Note implements Serializable {
    private String noteId;
    private String notebookId;
    private String title;
    private String summary;
    private String content;
    private Date createTime;
    private Date updateTime;
}
