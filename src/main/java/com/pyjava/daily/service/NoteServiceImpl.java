package com.pyjava.daily.service;

import com.google.inject.Singleton;
import com.pyjava.daily.entity.Notebook;
import com.pyjava.daily.mapper.NotebookMapper;
import com.pyjava.daily.util.JdbcUtil;
import javafx.scene.control.TreeItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/9 22:09
 */
@Singleton
public class NoteServiceImpl implements NoteService {

    @Override
    public List<Notebook> list() {
        List<Notebook> query;
        // 1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = JdbcUtil.getSqlSessionFactory();
        // 2、获取sqlSession对象
        try (SqlSession openSession = sqlSessionFactory.openSession()) {
            // 3、获取接口的实现类对象
            //会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
            NotebookMapper mapper = openSession.getMapper(NotebookMapper.class);
            query = mapper.list();
        }
        return query;
    }

    @Override
    public TreeItem<String> buildTree(List<Notebook> notebookList){
        List<Notebook> notebookTree = getNotebookTree(notebookList);
        if (CollectionUtils.isEmpty(notebookTree)) {
            return null;
        }
        TreeItem<String> root = new TreeItem<>();
        for (Notebook notebook : notebookTree) {
            TreeItem<String> sec = new TreeItem<>(notebook.getName());
            buildTree(notebook, sec);
            root.getChildren().add(sec);
        }
        return root;
    }

    private List<Notebook> getNotebookTree(List<Notebook> notebookList) {
        if (CollectionUtils.isEmpty(notebookList)) {
            return null;
        }
        // 先查找一级notebook
        List<Notebook> rootList = new ArrayList<>();
        for (Notebook notebook : notebookList) {
            if (StringUtils.isBlank(notebook.getParentId())) {
                rootList.add(notebook);
            }
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (Notebook notebook : notebookList) {
            notebook.setNotebooks(getChild(notebook.getNotebookId(), notebookList));
        }

        return rootList;
    }

    private List<Notebook> getChild(String id, List<Notebook> notebookList) {
        List<Notebook> children = new ArrayList<>();

        for (Notebook notebook : notebookList) {
            if (StringUtils.isNotBlank(notebook.getParentId())) {
                if (notebook.getParentId().equals(id)) {
                    children.add(notebook);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (Notebook notebook : children) {
            // 没有url子菜单还有子菜单
            if (StringUtils.isBlank(notebook.getParentId())) {
                // 递归
                notebook.setNotebooks(getChild(notebook.getNotebookId(), notebookList));
            }
        }
        return children;
    }

    private void buildTree(Notebook notebook, TreeItem<String> rootItem) {
        List<Notebook> children = notebook.getNotebooks();
        if (children == null) {
            return;
        }
        for (Notebook child : children) {
            TreeItem<String> item = new TreeItem<>(child.getName());
            rootItem.getChildren().add(item);
            buildTree(child, item);
        }
    }
}
