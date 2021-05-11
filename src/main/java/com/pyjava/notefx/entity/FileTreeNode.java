package com.pyjava.notefx.entity;

import com.pyjava.notefx.constants.Constants;
import com.pyjava.notefx.constants.FileType;

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
    private FileType fileType;
    private File file;
    private final List<FileTreeNode> children;

    public FileTreeNode(File file) {
        this.name = file.getName();
        this.file = file;
        this.isExpanded = false;
        this.children = new ArrayList<>();
        // 判断文件类型
        if(file.isFile()){
            String extension = file.getName().substring(file.getName().lastIndexOf("."));
            if (Constants.MD.equals(extension)) {
                this.fileType = FileType.MD;
            } else if (Constants.TXT.equals(extension)) {
                this.fileType = FileType.TXT;
            }
        }
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

    public FileType getFileType() {
        return fileType;
    }
}
