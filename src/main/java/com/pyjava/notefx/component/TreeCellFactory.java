package com.pyjava.notefx.component;

import com.pyjava.notefx.controller.MainController;
import com.pyjava.notefx.entity.FileTreeNode;
import com.pyjava.notefx.thread.NoteFxThreadPool;
import javafx.collections.ObservableList;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.reactfx.EventStreams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/8 1:09
 * @since 1.0
 */
public class TreeCellFactory extends TreeCell<FileTreeNode> {

    private MainController mainController;

    public TreeCellFactory(MainController mainController) {
        EventStreams.eventsOf(this, MouseEvent.MOUSE_CLICKED)
                .filter(mouseEvent ->
                        getItem() != null && !getItem().getFile().isDirectory()
                                && mouseEvent.getButton() == MouseButton.PRIMARY
                                && mouseEvent.getClickCount()==2)
                .subscribe(mouseEvent -> {
                    FileTreeNode item = getItem();
                    ObservableList<Tab> tabs = mainController.rightTab.getTabs();
                    FileTab fileTab = new FileTab(item.getFile());
                    fileTab.setText(item.getName());
                    // 将空内容加入tab中
                    TextArea textArea = fileTab.getTextArea();
                    FutureTask<String> call = new FutureTask<>(() -> {
                        StringBuilder sb = new StringBuilder();
                        try (BufferedReader br = new BufferedReader(new FileReader(item.getFile(), StandardCharsets.UTF_8))) {
                            br.lines().map(s -> s + "\n").forEach(sb::append);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "";
                        }
                        // 返回读取文件数据
                        return sb.toString();
                    });
                    NoteFxThreadPool.getThreadPool().submit(call);
                    String fileContent = "";
                    try {
                        fileContent = call.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    textArea.setText(fileContent);
                    fileTab.setContent(textArea);
                    tabs.add(fileTab);
                    SingleSelectionModel<Tab> selectionModel = mainController.rightTab.getSelectionModel();
                    selectionModel.select(fileTab);
                });
    }

    @Override
    public void updateItem(FileTreeNode item, boolean empty) {
        super.updateItem(item, empty);
        this.setText(null);
        this.setGraphic(null);
        if (null != item) {
            setText(item.getName());
            setGraphic(getTreeItem().getGraphic());
        }
    }
}
