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
public interface NotebookService {
    /**
     * 查询所有笔记本数据
     * @return {@link Notebook} list.
     * (it guarantees that this function returns array )
     */
    List<Notebook> list();

    /**
     * 保存笔记本
     * @param notebook 笔记本实体
     * @return 是否保存成功
     */
    Boolean save(Notebook notebook);

    /**
     * 保存笔记本
     * @param notebook 笔记本实体
     * @return 是否保存成功
     */
    Boolean update(Notebook notebook);

    /**
     * 根据id删除笔记本
     * @param notebookId 笔记本id
     * @return 是否删除
     */
    Boolean deleteById(String notebookId);

    /**
     * 根据id列表递归删除笔记本
     * @param ids id数组
     * @return 是否删除
     */
    Boolean deleteByIds(List<String> ids);

    /**
     * 根据笔记本列表构建tree item
     * @param notebookList 笔记本列表
     * @return {@link TreeItem}
     */
    TreeItem<Notebook> buildTree(List<Notebook> notebookList);
}
