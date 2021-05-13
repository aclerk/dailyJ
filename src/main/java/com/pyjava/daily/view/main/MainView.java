package com.pyjava.daily.view.main;

import com.pyjava.daily.Starter;
import com.pyjava.daily.viewmodel.main.MainViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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
    public BorderPane treeView;
    @FXML
    public BorderPane rightTab;

    @InjectViewModel
    private MainViewModel viewModel;

    @Inject
    private NotificationCenter notificationCenter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        splitPane.getStyleClass().add("main-split");

        // 监听窗口宽度改变
        rootBorderPane.widthProperty().addListener((observableValue, number, t1) -> splitPane.setDividerPosition(0, 0.2));

        // 监听窗口关闭
        notificationCenter.subscribe("exit", (key, payload) -> {
            logger.debug("key={},payload={}", key, payload);
            Starter.getMain().close();
        });


    }

}