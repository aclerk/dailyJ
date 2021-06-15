package com.pyjava.daily.service;

import com.pyjava.daily.entity.Notebook;
import javafx.scene.control.TreeItem;

import java.util.List;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/15 17:28
 */
public interface NoteService {
    /**
     * 查询所有笔记本数据
     * @return {@link Notebook} list
     */
    List<Notebook> list();

    /**
     * 根据笔记本列表构建tree item
     * @param notebookList 笔记本列表
     * @return {@link TreeItem}
     */
    TreeItem<String> buildTree(List<Notebook> notebookList);
}
