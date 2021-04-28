package com.pyjava.notefx.controller;

import com.pyjava.notefx.Main;
import com.pyjava.notefx.component.FileTab;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.reactfx.Change;
import org.reactfx.EventStreams;

import java.io.*;
import java.net.URL;
import java.util.Objects;
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

    private final Image penIcon = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("icon/pen-16.png")));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventStreams.changesOf(rootPane.widthProperty()).subscribe(this::changeWidth);
        splitPane.getStyleClass().add("main-split");
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
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
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
        FileTab tab = new FileTab(fileName, file);
        // 将文件内容加入tab中
        TextArea textArea = new TextArea();
        textArea.setText(fileContent);
        tab.setContent(textArea);
        // 监听文本区域值改变,当改变时,提供修改图标,提示用户该文件已被修改
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            ImageView iv = new ImageView(penIcon);
            iv.setSmooth(true);
            iv.setViewport(new Rectangle2D(0, 0, 16, 16));
            tab.setGraphic(iv);
        });
        textArea.setOnKeyPressed((event) -> {
            if(event.isControlDown() && event.getCode().getName().equals(KeyCode.S.getName())){
                tab.setGraphic(null);
                CompletableFuture.supplyAsync(()->{
                    FileWriter fileWriter = null;
                    try {
                        fileWriter = new FileWriter(tab.getFile());
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
        rightTab.getTabs().add(tab);

        // 将刚打开的文件的tab置为选中状态
        SingleSelectionModel<Tab> selectionModel = rightTab.getSelectionModel();
        selectionModel.select(tab);
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