package com.pyjava.notefx.controller;

import com.pyjava.notefx.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;

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
    public void openFile(){
        System.out.println("openFile");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开文件");
        File file = fileChooser.showOpenDialog(Main.get());
        String fileName = String.valueOf(file);
        long fileLengthLong = file.length();
        byte[] fileContent = new byte[(int) fileLengthLong];

        try {
            FileInputStream inputStream = new FileInputStream(file);
            int read = inputStream.read(fileContent);
            inputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String string = new String(fileContent);
        System.out.println(string);
    }

    @FXML
    public void openHexo(){
        System.out.println("openHexo");
    }

    @FXML
    public void exit(){
        Main.get().close();
        Platform.exit();
    }
}