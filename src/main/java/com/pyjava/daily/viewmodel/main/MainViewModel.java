package com.pyjava.daily.viewmodel.main;

import com.pyjava.daily.model.FileTreeNode;
import com.pyjava.daily.thread.FileMonitor;
import de.saxsys.mvvmfx.ViewModel;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;

import javax.inject.Singleton;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static com.pyjava.daily.constants.Resource.FILE_ICON;
import static com.pyjava.daily.constants.Resource.FOLDER_ICON;

/**
 * <p>描述: 主视图vm </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/12 21:43
 */
@Singleton
public class MainViewModel implements ViewModel {

    private FileTreeNode fileTreeNode;
    private TreeView<FileTreeNode> treeView;
    private File dir;

    public void setTreeView(TreeView<FileTreeNode> treeView) {
        this.treeView = treeView;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public void update() {
        fileTreeNode = new FileTreeNode(dir.getName());
        fileTreeNode.setFile(dir);
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