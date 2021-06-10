package com.pyjava.daily.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (Notebook)实体类
 *
 * @author zhaojj11
 * @version 1.0
 * @date 2021-06-10 18:33:49
 */
@Data
public class Notebook implements Serializable {
    private static final long serialVersionUID = -95579223172052011L;

    private String notebookId;

    private String parentId;

    private String name;

    private Date createTime;

    private Date updateTime;

    private List<Notebook> notebooks;
}
