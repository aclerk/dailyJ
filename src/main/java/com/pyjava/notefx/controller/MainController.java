package com.pyjava.notefx.controller;

import com.pyjava.notefx.Main;
import com.pyjava.notefx.component.FileTab;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.reactfx.Change;
import org.reactfx.EventStreams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/4/25 14:18
 */
public class MainController implements Initializable {

    public static int untitledCount = 1;

    @FXML
    public StackPane root;
    @FXML
    public BorderPane rootPane;
    @FXML
    public Label coordinate;
    @FXML
    public Label textType;
    @FXML
    public SplitPane splitPane;
    @FXML
    public BorderPane leftPane;
    @FXML
    public TabPane rightTab;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventStreams.changesOf(rootPane.widthProperty()).subscribe(this::changeWidth);
        splitPane.getStyleClass().add("main-split");
        ObservableList<Tab> tabs = rightTab.getTabs();
        if(tabs.size() <= 0){
            FileTab fileTab = new FileTab();
            fileTab.setText("Untitled-"+untitledCount);
            // 将空内容加入tab中
            TextArea textArea = fileTab.getTextArea();
            textArea.setText("");
            fileTab.setContent(textArea);
            untitledCount++;
            tabs.add(fileTab);
        }
    }

    @FXML
    public void createFile(){
        // 新建文件时 展示未命名tab
        ObservableList<Tab> tabs = rightTab.getTabs();
        FileTab fileTab = new FileTab();
        fileTab.setText("Untitled-"+untitledCount);
        untitledCount++;
        // 将空内容加入tab中
        TextArea textArea = fileTab.getTextArea();
        textArea.setText("");
        fileTab.setContent(textArea);
        tabs.add(fileTab);
        // 将刚打开的文件的tab置为选中状态
        SingleSelectionModel<Tab> selectionModel = rightTab.getSelectionModel();
        selectionModel.select(fileTab);
    }

    @FXML
    public void createHexo(){
        System.out.println("create file");
    }


    @FXML
    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开文件");
        File file = fileChooser.showOpenDialog(Main.get());

        // 查看当前文件是否已经被打开
        int index = -1;
        ObservableList<Tab> tabs = rightTab.getTabs();
        for (int i = 0; i < tabs.size(); i++) {
            FileTab tab = (FileTab) tabs.get(i);
            File f = tab.getFile();
            // unTitled的文件
            if(null == f){
                break;
            }
            boolean equals = f.getAbsolutePath().equals(file.getAbsolutePath());
            if (equals) {
                index = i;
                break;
            }
        }

        // 如果已经打开该文件,则将该文件变成选择状态
        if (index != -1) {
            SingleSelectionModel<Tab> selectionModel = rightTab.getSelectionModel();
            selectionModel.select(index);
            return;
        }

        // 如果该文件还没有打开,则
        String fileName = file.getName();
        String fileContent = CompletableFuture.supplyAsync(() -> {
            // 读取文件时,鼠标指针为WAIT状态
            rootPane.setCursor(Cursor.WAIT);
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                br.lines().map(s -> s + "\n").forEach(sb::append);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
            // 读取完成将鼠标恢复
            rootPane.setCursor(Cursor.DEFAULT);
            // 返回读取文件数据
            return sb.toString();
        }).join();
        // 添加tab
        FileTab fileTab = new FileTab(fileName, file);
        // 将文件内容加入tab中
        TextArea textArea = fileTab.getTextArea();
        textArea.setText(fileContent);
        fileTab.setContent(textArea);

        rightTab.getTabs().add(fileTab);

        // 将刚打开的文件的tab置为选中状态
        SingleSelectionModel<Tab> selectionModel = rightTab.getSelectionModel();
        selectionModel.select(fileTab);
    }

    @FXML
    public void openHexo() {
        System.out.println("openHexo");
    }

    @FXML
    public void exit() {
        Main.get().close();
        Platform.exit();
    }

    private void changeWidth(Change<Number> numberChange) {
        splitPane.setDividerPosition(0, 0.2);
    }
}