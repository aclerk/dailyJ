package com.pyjava.daily.controller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/14 17:54
 */
public class NewDialogController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(NewDialogController.class);
    @FXML
    public TextField dailyName;
    @FXML
    public TextField dailyLocation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void openDirectory() {

    }

    public void submit() {

    }

    public void cancel() {

    }
}
