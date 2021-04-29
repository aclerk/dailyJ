package com.pyjava.notefx.component;

import com.pyjava.notefx.Main;
import com.pyjava.notefx.constants.Resource;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/4/28 0:41
 */
public class FileTab extends Tab {
    private File file;
    private TextArea textArea = new TextArea();

    public FileTab() {
        super();
        this.addListener();
    }

    public FileTab(File file) {
        super();
        this.file = file;
        this.addListener();
    }

    public FileTab(String s, File file) {
        super(s);
        this.file = file;
        this.addListener();
    }

    public FileTab(String s, Node node, File file) {
        super(s, node);
        this.file = file;
        this.addListener();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    private void addListener(){
        // 监听文本区域值改变,当改变时,提供修改图标,提示用户该文件已被修改
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            ImageView iv = new ImageView(Resource.PEN_ICON);
            iv.setSmooth(true);
            iv.setViewport(new Rectangle2D(0, 0, 16, 16));
            this.setGraphic(iv);
        });
        // 监听按键
        textArea.setOnKeyPressed((event) -> {
            // 监听按键 当ctrl和s键按下时触发文件保存功能
            if(event.isControlDown() && event.getCode().getName().equals(KeyCode.S.getName())){
                if(null == this.getFile()){
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                    FileChooser.ExtensionFilter mdFilter = new FileChooser.ExtensionFilter("Markdown files (*.md)", "*.md");
                    fileChooser.getExtensionFilters().addAll(txtFilter, mdFilter);
                    fileChooser.setTitle("保存文件");
                    File file = fileChooser.showSaveDialog(Main.get());
                    this.setFile(file);
                    String fileName = file.getName();
                    this.setText(fileName);
                }
                this.setGraphic(null);
                CompletableFuture.supplyAsync(()->{
                    FileWriter fileWriter = null;
                    try {
                        fileWriter = new FileWriter(this.getFile(),  StandardCharsets.UTF_8);
                        // 写入文件
                        fileWriter.write(textArea.getText());
                        fileWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(fileWriter != null){
                            try {
                                fileWriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    return null;
                });
            }
        });
    }
}
