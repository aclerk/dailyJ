package com.pyjava.notefx.controller;

import com.pyjava.notefx.Main;
import com.pyjava.notefx.component.FileTab;
import com.pyjava.notefx.component.TreeCellFactory;
import com.pyjava.notefx.entity.FileTreeNode;
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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

import static com.pyjava.notefx.constants.Resource.FILE_ICON;
import static com.pyjava.notefx.constants.Resource.FOLDER_ICON;

/**
 * <p>描述: 主视图控制器 </p>
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
    public TabPane rightTab;
    @FXML
    public TreeView<FileTreeNode> treeView;

    /**
     * 未命名文件计数
     */
    public static int untitledCount = 1;

    /**
     * 文件树对象
     */
    private FileTreeNode fileTreeNode;

    /**
     * 工作目录
     */
    private File myDir;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 监听主面板长度更新时,同步更新面板比例
        EventStreams.changesOf(rootPane.widthProperty()).subscribe(this::changeWidth);
        splitPane.getStyleClass().add("main-split");
        // 初始化创建时创建新未命名tab
        ObservableList<Tab> tabs = rightTab.getTabs();
        if (tabs.size() <= 0) {
            FileTab fileTab = new FileTab();
            fileTab.setText("Untitled-" + untitledCount);
            // 将空内容加入tab中
            TextArea textArea = fileTab.getTextArea();
            textArea.setText("");
            fileTab.setContent(textArea);
            untitledCount++;
            tabs.add(fileTab);
        }
        treeView.setEditable(false);
        treeView.setCellFactory(param -> new TreeCellFactory(this));
    }

    @FXML
    public void createFile() {
        // 新建文件时 展示未命名tab
        ObservableList<Tab> tabs = rightTab.getTabs();
        FileTab fileTab = new FileTab();
        fileTab.setText("Untitled-" + untitledCount);
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
    public void createNote() {
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
            if (null == f) {
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
        if (file != null) {
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
    public void openFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(Main.get());
        if(dir == null){
            return;
        }
        this.myDir = dir;

        fileTreeNode = new FileTreeNode(dir.getName());
        fileTreeNode.setExpanded(true);
        fileTreeNode.setFile(dir);
        iterateFiles(fileTreeNode);

        if (dir.exists()) {
            LinkedList<File> fileLinkedList = new LinkedList<>();
            fileLinkedList.add(dir);
            while (fileLinkedList.size() > 0) {
                File f = fileLinkedList.removeFirst();
                File[] files = f.listFiles();
                if (files == null) {
                    continue;
                }
                for (File file : files) {
                    if (file.isDirectory()) {
                        fileLinkedList.addLast(file);
                        FileMonitor.get().addWatchFile(file);
                    }
                }
            }

            ImageView iv = new ImageView(FOLDER_ICON);
            iv.setSmooth(true);
            iv.setViewport(new Rectangle2D(0, 0, 16, 16));
            TreeItem<FileTreeNode> rootTree = new TreeItem<>(fileTreeNode, iv);
            buildTree(fileTreeNode, rootTree);
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

    private void changeWidth(Change<Number> numberChange) {
        splitPane.setDividerPosition(0, 0.2);
    }

    public void update() {
        File dir = myDir;

        ImageView iv = new ImageView(FOLDER_ICON);
        iv.setSmooth(true);
        iv.setViewport(new Rectangle2D(0, 0, 16, 16));
        TreeItem<FileTreeNode> rootTree = new TreeItem<>(fileTreeNode, iv);

        // 获取新文件树
        FileTreeNode changedFileTreeNode = new FileTreeNode(dir.getName());
        changedFileTreeNode.setExpanded(fileTreeNode.getExpanded());
        changedFileTreeNode.setFile(fileTreeNode.getFile());

        iterateFiles(changedFileTreeNode);
        // 将根据新文件树,将旧文件树数据拷贝过来
        diffTwoTree(fileTreeNode, changedFileTreeNode);
        fileTreeNode = changedFileTreeNode;

        buildTree(fileTreeNode, rootTree);
        Platform.runLater(() -> {
            treeView.setRoot(rootTree);
            rootTree.setExpanded(true);
        });
        FileMonitor.get().stopWatch();

        LinkedList<File> fileLinkedList = new LinkedList<>();
        fileLinkedList.add(dir);
        while (fileLinkedList.size() > 0) {
            File f = fileLinkedList.removeFirst();
            File[] files = f.listFiles();
            if (files == null) {
                continue;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    fileLinkedList.addLast(file);
                    FileMonitor.get().addWatchFile(file);
                }
            }
        }

        FileMonitor.get().addWatchFile(dir);
        FileMonitor.get().setListener(this);
        FileMonitor.get().watch();
    }


    /**
     * <p>遍历文件</p>
     *
     * @param fileTreeNode : {@link FileTreeNode} 文件树节点
     * @author zhaojj11
     * @date 2021/5/8 0:51
     * @since 1.0
     */
    private void iterateFiles(FileTreeNode fileTreeNode) {
        File file = fileTreeNode.getFile();

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (null == files || files.length <= 0) {
                return;
            }

            for (File fi : files) {
                FileTreeNode fti = new FileTreeNode(fi.getName());
                fti.setExpanded(false);
                fti.setFile(fi);
                fileTreeNode.getChildren().add(fti);
                iterateFiles(fti);
            }
        } else {
            FileTreeNode fti = new FileTreeNode(file.getName());
            fti.setExpanded(false);
            fti.setFile(file);
            fileTreeNode.getChildren().add(fti);
        }
    }

    /**
     * <p>
     *     对比旧文件树和触发更新后的文件树,将新文件树的节点更新到旧文件树上
     * </p>
     *
     * @param fileTreeNode :  {@link FileTreeNode} 旧文件树
     * @param changedFileTreeNode : {@link FileTreeNode} 旧文件树
     * @author zhaojj11
     * @date 2021/5/8 0:53
     * @since 1.0
     */
    private void diffTwoTree(FileTreeNode fileTreeNode, FileTreeNode changedFileTreeNode) {
        if (!fileTreeNode.getName().equals(changedFileTreeNode.getName())) {
            return;
        }

        File file = changedFileTreeNode.getFile();
        if (file.isDirectory()) {
            changedFileTreeNode.setExpanded(fileTreeNode.getExpanded());
            List<FileTreeNode> children = fileTreeNode.getChildren();
            List<FileTreeNode> children1 = changedFileTreeNode.getChildren();
            if (children == null || children1 == null) {
                return;
            }
            for (FileTreeNode child : children) {
                for (FileTreeNode treeItem : children1) {
                    diffTwoTree(child, treeItem);
                }
            }
        } else if (file.isFile()) {
            changedFileTreeNode.setExpanded(false);
        }
    }

    /**
     * <p>
     *     根据{@link FileTreeNode} 构建 {@link TreeItem}
     * </p>
     *
     * @param fileTreeNode : 文件树节点
     * @param rootItem : TreeItem
     * @author zhaojj11
     * @date 2021/5/8 0:54
     * @since 1.0
     */
    private void buildTree(FileTreeNode fileTreeNode, TreeItem<FileTreeNode> rootItem) {
        List<FileTreeNode> children = fileTreeNode.getChildren();
        if (children == null) {
            return;
        }
        Consumer<FileTreeNode> consumer = fti -> {
            File fi = fti.getFile();
            TreeItem<FileTreeNode> item = new TreeItem<>(fti);
            if (fi.isDirectory()) {
                ImageView iv = new ImageView(FOLDER_ICON);
                iv.setSmooth(true);
                iv.setViewport(new Rectangle2D(0, 0, 16, 16));
                item.setGraphic(iv);
                item.setExpanded(fti.getExpanded());
                rootItem.getChildren().add(item);
                buildTree(fti, item);
            } else {
                item.setGraphic(new ImageView(FILE_ICON));
                rootItem.getChildren().add(item);
            }
            item.addEventHandler(TreeItem.branchExpandedEvent(),
                    objectTreeModificationEvent -> fti.setExpanded(objectTreeModificationEvent.wasExpanded()));
        };
        children.stream().filter(f -> !f.getFile().isHidden() && f.getFile().isDirectory()).sorted().forEach(consumer);
        children.stream().filter(f -> !f.getFile().isHidden() && f.getFile().isFile()).sorted().forEach(consumer);
    }
}