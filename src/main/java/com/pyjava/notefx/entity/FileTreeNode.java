package com.pyjava.notefx.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/8 1:10
 * @since 1.0
 */
public class FileTreeNode implements Comparable<FileTreeNode> {
    private final String name;
    private Boolean isExpanded;
    private File file;
    private final List<FileTreeNode> children;

    public FileTreeNode(String name) {
        this.name = name;
        this.isExpanded = false;
        this.children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public List<FileTreeNode> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(FileTreeNode o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }
}
