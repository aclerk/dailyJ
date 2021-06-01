package com.pyjava.daily.view.main;

import com.pyjava.daily.viewmodel.main.MainViewModel;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
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
    public SplitPane splitPane;
    @FXML
    public TabPane leftTabPane;
    @FXML
    public Tab inbox;
    @FXML
    public Tab noteTab;
    @FXML
    public TreeView<String> fileTree;
    @FXML
    public BorderPane main;
    @FXML
    public TabPane contentTab;
    @FXML
    public Tab todo;
    @FXML
    public Tab account;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}