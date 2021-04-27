package com.pyjava.notefx.component;

import javafx.scene.Node;
import javafx.scene.control.Tab;

import java.io.File;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/4/28 0:41
 */
public class FileTab extends Tab {
    private File file;

    public FileTab() {
        super();
    }

    public FileTab(File file) {
        super();
        this.file = file;
    }

    public FileTab(String s, File file) {
        super(s);
        this.file = file;
    }

    public FileTab(String s, Node node, File file) {
        super(s, node);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
