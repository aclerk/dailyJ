package com.pyjava.daily.view.main;

import com.pyjava.daily.Starter;
import com.pyjava.daily.config.Config;
import com.pyjava.daily.model.FileTreeNode;
import com.pyjava.daily.thread.FileMonitor;
import com.pyjava.daily.viewmodel.main.MainViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
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

    @InjectViewModel
    private MainViewModel mainViewModel;

    @Inject
    private NotificationCenter notificationCenter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FileMonitor.setMainViewModel(mainViewModel);
        splitPane.getStyleClass().add("main-split");
        main.setCenter(new Text("hello, this is daily"));
        String lastFilePath = Config.getLastFilePath();
        if(StringUtils.isNotEmpty(lastFilePath)){
            // 说明曾经打开过,直接打开
            leftTabPane.setMinWidth(50);
            splitPane.setDisable(false);
            splitPane.setDividerPosition(0, 0.2);
            rootBorderPane.widthProperty().addListener((observableValue, number, t1) -> splitPane.setDividerPosition(0, 0.2));
            mainViewModel.setTreeView(fileTree);
            mainViewModel.setDir(new File(lastFilePath));
            mainViewModel.update();
        }else{
            // 还没有打开过,
            // 还没有选择工作空间的时候,把分割符放到最左边展示项目文件介绍
            splitPane.setDividerPosition(0,0);
            splitPane.setDividerPosition(1,1);
            splitPane.setDisable(true);
            rootBorderPane.widthProperty().addListener((observableValue, number, t1) -> splitPane.setDividerPosition(0, 0));
        }

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