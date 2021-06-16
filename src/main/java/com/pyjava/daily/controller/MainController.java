package com.pyjava.daily.controller;

import com.google.inject.Inject;
import com.pyjava.daily.Starter;
import com.pyjava.daily.notification.NotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
public class MainController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    @FXML
    public BorderPane rootBorderPane;
    @FXML
    public TabPane leftTabPane;
    @FXML
    public Tab noteTab;
    @Inject
    private NotificationCenter notificationCenter;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notificationCenter.subscribe("exit", (key, payload) -> {
            logger.debug("key={},payload={}", key, payload);
            Starter.getMain().close();
        });
    }

}