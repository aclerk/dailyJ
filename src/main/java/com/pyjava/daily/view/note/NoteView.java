package com.pyjava.daily.view.note;

import com.pyjava.daily.entity.Notebook;
import com.pyjava.daily.view.newdialog.NewDialogView;
import com.pyjava.daily.viewmodel.NoteViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/9 22:07
 */
public class NoteView implements FxmlView<NoteViewModel>, Initializable {
    private static final Logger logger = LoggerFactory.getLogger(NewDialogView.class);
    @FXML
    public StackPane notePane;
    @FXML
    public SplitPane splitPane;
    @FXML
    public TreeView<String> leftTree;
    @FXML
    public TabPane rightPane;
    @FXML
    public StackPane areaPane;

    @InjectViewModel
    NoteViewModel noteViewModel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        splitPane.setDividerPosition(0, 0.2);
        splitPane.setDividerPosition(1, 0.4);
        notePane.widthProperty().addListener((observableValue, number, t1) -> splitPane.setDividerPosition(0, 0.2));

        // 目录树中添加菜单那
        ContextMenu menu = new ContextMenu();
        MenuItem addCatalog = new MenuItem("新建目录");
        MenuItem deleteItem = new MenuItem("删除");
        MenuItem renameItem = new MenuItem("重命名");
        menu.getItems().add(addCatalog);
        menu.getItems().add(deleteItem);
        menu.getItems().add(renameItem);
        leftTree.setContextMenu(menu);
        List<Notebook> notebooks = noteViewModel.list();
        TreeItem<String> items = noteViewModel.buildTree(notebooks);
        leftTree.setRoot(items);
        leftTree.setShowRoot(false);
    }
}
