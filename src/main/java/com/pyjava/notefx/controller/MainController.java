package com.pyjava.notefx.controller;

import com.pyjava.notefx.Main;
import com.pyjava.notefx.component.FileTab;
import com.pyjava.notefx.file.FileMonitor;
import com.pyjava.notefx.thread.NoteFxThreadPool;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.reactfx.Change;
import org.reactfx.EventStreams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

import static com.pyjava.notefx.constants.Resource.FILE_ICON;
import static com.pyjava.notefx.constants.Resource.FOLDER_ICON;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/4/25 14:18
 */
public class MainController implements Initializable, Runnable {

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
    public TabPane rightTab;
    @FXML
    public TreeView<File> treeView;


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
    public void createNote(){
        System.out.println("create file");
    }


    @FXML
    public void openFile() throws ExecutionException, InterruptedException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开文件");
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter mdFilter = new FileChooser.ExtensionFilter("Markdown files (*.md)", "*.md");
        fileChooser.getExtensionFilters().addAll(txtFilter, mdFilter);
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
        if(file != null){
            String fileName = file.getName();

            FutureTask<String> call = new FutureTask<>(() -> {
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
            });
            NoteFxThreadPool.getThreadPool().submit(call);
            String fileContent = call.get();

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
    }

    @FXML
    public void openFolder(){
        System.out.println("open folder");
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(Main.get());

        if(dir != null && dir.exists()){
            LinkedList<File> fileLinkedList = new LinkedList<>();
            fileLinkedList.add(dir);
            while(fileLinkedList.size() > 0){
                File f = fileLinkedList.removeFirst();
                File[] files = f.listFiles();
                if(files == null){
                    continue;
                }
                for (File file : files) {
                    if(file.isDirectory()){
                        fileLinkedList.addLast(file);
                        FileMonitor.get().addWatchFile(file);
                    }
                }
            }

            ImageView iv = new ImageView(FOLDER_ICON);
            iv.setSmooth(true);
            iv.setViewport(new Rectangle2D(0, 0, 16, 16));
            TreeItem<File> rootTree = new TreeItem<>(dir, iv);
            searchFile(dir, rootTree);
            treeView.setRoot(rootTree);
            rootTree.setExpanded(true);
            FileMonitor.get().addWatchFile(dir);
            FileMonitor.get().setListener(this);
            FileMonitor.get().watch();
        }
    }

    @FXML
    public void openNote() {
        System.out.println("openNote");
    }

    @FXML
    public void exit() {
        Main.get().close();
        Platform.exit();
    }

    private void searchFile(File fileOrDir, TreeItem<File> rootItem) {
        File[] list = fileOrDir.listFiles();
        if (list == null) {
            return;
        }
        Consumer<File> consumer = f -> {
            TreeItem<File> item = new TreeItem<>(f);
            if (f.isDirectory()) {
                ImageView iv = new ImageView(FOLDER_ICON);
                iv.setSmooth(true);
                iv.setViewport(new Rectangle2D(0, 0, 16, 16));
                item.setGraphic(iv);
                rootItem.getChildren().add(item);
                searchFile(f, item);
            } else {
                item.setGraphic(new ImageView(FILE_ICON));
                rootItem.getChildren().add(item);
            }
        };
        Arrays.stream(list).filter(f -> !f.isHidden() && f.isDirectory()).sorted().forEach(consumer);
        Arrays.stream(list).filter(f -> !f.isHidden() && f.isFile()).sorted().forEach(consumer);
    }

    private void changeWidth(Change<Number> numberChange) {
        splitPane.setDividerPosition(0, 0.2);
    }

    @Override
    public void run() {
        TreeItem<File> rootTree = treeView.getRoot();
        File dir = rootTree.getValue();

        ImageView iv = new ImageView(FOLDER_ICON);
        iv.setSmooth(true);
        iv.setViewport(new Rectangle2D(0, 0, 16, 16));
        TreeItem<File> root = new TreeItem<>(dir, iv);
        searchFile(dir, root);
        Platform.runLater(() -> {
            treeView.setRoot(root);
            root.setExpanded(true);
        });
        FileMonitor.get().stopWatch();

        LinkedList<File> fileLinkedList = new LinkedList<>();
        fileLinkedList.add(dir);
        while(fileLinkedList.size() > 0){
            File f = fileLinkedList.removeFirst();
            File[] files = f.listFiles();
            if(files == null){
                continue;
            }
            for (File file : files) {
                if(file.isDirectory()){
                    fileLinkedList.addLast(file);
                    FileMonitor.get().addWatchFile(file);
                }
            }
        }

        FileMonitor.get().addWatchFile(dir);
        FileMonitor.get().setListener(this);
        FileMonitor.get().watch();
    }
}