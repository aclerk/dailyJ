package com.pyjava.daily.view.main;

import com.pyjava.daily.Starter;
import com.pyjava.daily.config.Config;
import com.pyjava.daily.model.FileTreeNode;
import com.pyjava.daily.thread.FileMonitor;
import com.pyjava.daily.util.FileUtil;
import com.pyjava.daily.viewmodel.main.MainViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>描述: 主窗口视图 </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/12 21:42
 */
public class MainView implements FxmlView<MainViewModel>, Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainView.class);

    @FXML
    public BorderPane rootBorderPane;

    @FXML
    public TabPane leftTabPane;
    @FXML
    public BorderPane main;
    @FXML
    public SplitPane splitPane;
    @FXML
    public Tab noteTab;
    @FXML
    public Tab planTab;
    @FXML
    public Tab bookKeepingTab;
    @FXML
    public TreeView<FileTreeNode> fileTree;
    @FXML
    public TabPane contentTab;

    @InjectViewModel
    private MainViewModel mainViewModel;

    @Inject
    private NotificationCenter notificationCenter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FileMonitor.setMainViewModel(mainViewModel);
        splitPane.getStyleClass().add("main-split");
        String lastFilePath = Config.getLastFilePath();
        if (StringUtils.isNotEmpty(lastFilePath)) {
            // 说明曾经打开过,直接打开
            leftTabPane.setMinWidth(50);
            splitPane.setDisable(false);
            splitPane.setDividerPosition(0, 0.2);
            rootBorderPane.widthProperty().addListener((observableValue, number, t1) -> splitPane.setDividerPosition(0, 0.2));
            mainViewModel.setTreeView(fileTree);
            mainViewModel.setDir(new File(lastFilePath));
            mainViewModel.update();
        } else {
            // 还没有打开过,
            // 还没有选择工作空间的时候,把分割符放到最左边展示项目文件介绍
            splitPane.setDividerPosition(0, 0);
            splitPane.setDividerPosition(1, 1);
            splitPane.setDisable(true);
            rootBorderPane.widthProperty().addListener((observableValue, number, t1) -> splitPane.setDividerPosition(0, 0));
        }

        // 文件树添加右击菜单
        ContextMenu menu = new ContextMenu();
        Menu addMenu = new Menu("新建");
        MenuItem addFolder = new MenuItem("新建文件夹");
        addFolder.setOnAction(e -> {
            TreeItem<FileTreeNode> selectItem = fileTree.getSelectionModel().getSelectedItem();
            logger.debug("在{}新建文件夹", selectItem.getValue().getName());
            FileTreeNode node = selectItem.getValue();
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Text Input Dialog");
            dialog.setContentText("Please enter folder name:");
            Optional<String> s = dialog.showAndWait();
            if (s.isPresent()) {
                String absolutePath = node.getFile().getAbsolutePath();
                String dirPath = absolutePath + "\\" + s.get();
                logger.debug("dir path:{}", dirPath);
                File file = new File(dirPath);
                if (!file.exists()) {
                    boolean mkdir = file.mkdir();
                }
            }
        });
        MenuItem addFile = new MenuItem("新建文件");
        addFile.setOnAction(e -> {
            TreeItem<FileTreeNode> selectItem = fileTree.getSelectionModel().getSelectedItem();
            FileTreeNode node = selectItem.getValue();
            logger.debug("在{}新建文件", selectItem.getValue().getName());
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Text Input Dialog");
            dialog.setContentText("Please enter file name:");
            Optional<String> s = dialog.showAndWait();
            if (s.isPresent()) {
                String absolutePath = node.getFile().getAbsolutePath();
                String dirPath = absolutePath + "\\" + s.get();
                logger.debug("dir path:{}", dirPath);
                File file = new File(dirPath);
                if (!file.exists()) {
                    try {
                        boolean newFile = file.createNewFile();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        addMenu.getItems().addAll(addFolder, addFile);
        MenuItem deleteItem = new MenuItem("删除");
        deleteItem.setOnAction(e -> {
            TreeItem<FileTreeNode> selectItem = fileTree.getSelectionModel().getSelectedItem();
            FileTreeNode node = selectItem.getValue();
            logger.debug("在{}删除文件", node.getName());
            String dbParentPath = lastFilePath + ".daily\\";
            String dbPath = dbParentPath + "daily.db";
            String workspace = node.getFile().getAbsolutePath();
            if (node.getFile().isDirectory()) {
                workspace += "\\";
            }
            boolean flag = lastFilePath.equals(workspace)
                    || dbParentPath.equals(workspace)
                    || dbPath.equals(workspace);
            if (flag) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cannot delete .daily/daily.db");
                alert.setContentText("the folder .daily or file daily.db cannot be deleted");
                alert.getButtonTypes().add(new ButtonType("OK"));
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("delete file");
                alert.setContentText("delete the file?");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent()){
                    if (result.get() == ButtonType.OK){
                        File file = selectItem.getValue().getFile();
                        FileUtil.deleteFile(file);
                        // 不知道这里为什么需要更新一下
                        mainViewModel.update();
                    }
                }
            }
        });
        MenuItem renameItem = new MenuItem("重命名");
        renameItem.setOnAction(e -> {
            TreeItem<FileTreeNode> selectItem = fileTree.getSelectionModel().getSelectedItem();
            FileTreeNode node = selectItem.getValue();
            logger.debug("在{}重命名文件/文件夹", node.getName());
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("rename Dialog");
            dialog.setContentText("Please enter file name:");
            Optional<String> s = dialog.showAndWait();
            s.ifPresent(value -> FileUtil.rename(node.getFile(), value));
        });
        menu.getItems().add(addMenu);
        menu.getItems().add(deleteItem);
        menu.getItems().add(renameItem);
        fileTree.setContextMenu(menu);


        // fileTree点击事件
        fileTree.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            logger.debug("{}", newValue.getValue().getName());
            FileTreeNode value = newValue.getValue();
            Tab tab = new Tab(value.getName());
            TextArea textArea = new TextArea();
            tab.setContent(textArea);
            contentTab.getTabs().add(tab);
        });

        // 监听窗口关闭
        notificationCenter.subscribe("exit", (key, payload) -> {
            logger.debug("key={},payload={}", key, payload);
            Starter.getMain().close();
        });

        notificationCenter.subscribe("split-change", (key, payload) -> {
            logger.debug("key={},payload={}", key, payload);
            splitPane.setDisable(false);
            leftTabPane.setMinWidth(50);
            splitPane.setDividerPosition(0, 0.2);
            rootBorderPane.widthProperty().addListener((observableValue, number, t1) -> splitPane.setDividerPosition(0, 0.2));
        });

    }

}