package com.pyjava.daily.mapper;

import com.pyjava.daily.entity.Notebook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/11 0:28
 */
@Mapper
public interface NotebookMapper {
    /**
     * 查询所有笔记本
     * @return notebook数组
     */
    List<Notebook> list();
}