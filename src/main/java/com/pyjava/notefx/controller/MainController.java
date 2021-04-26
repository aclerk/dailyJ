package com.pyjava.notefx.controller;

import com.pyjava.notefx.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;


/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/4/25 14:18
 */
public class MainController {


    @FXML
    public StackPane root;

    @FXML
    public BorderPane rootPane;

    @FXML
    public void createDir(){
        System.out.println("createDir");
    }

    @FXML
    public void exit(){
        Main.get().close();
        Platform.exit();
    }
}