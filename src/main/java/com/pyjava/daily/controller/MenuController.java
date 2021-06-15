package com.pyjava.daily.controller;

import com.pyjava.daily.Starter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
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

    public static Stage stage = null;
    public static Scene scene = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void createDaily() {

    }

    public void openDaily() {

    }

    public void exit() {
        Starter.getMain().close();
        Platform.exit();
    }
}
