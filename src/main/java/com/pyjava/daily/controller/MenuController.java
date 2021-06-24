package com.pyjava.daily.controller;

import com.google.inject.Inject;
import com.pyjava.daily.notification.NotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * <p>描述: 菜单视图 </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/12 23:07
 */
public class MenuController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @FXML
    private BorderPane menuViewPane;

    @Inject
    private NotificationCenter notificationCenter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void exit() {
        notificationCenter.publish("exit");
    }
}
