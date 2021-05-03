package com.pyjava.notefx;

import com.pyjava.notefx.file.FileMonitor;
import com.pyjava.notefx.thread.NoteFxThreadPool;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/4/25 14:17
 */
public class Main extends Application {

    private static Stage main;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage get() {
        return main;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        main = primaryStage;

        URL resource = getClass().getClassLoader().getResource("fxml/main.fxml");
        URL iconRes = getClass().getClassLoader().getResource("img/noteFx.png");
        assert resource != null && iconRes != null;
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root, 1000, 618);
        primaryStage.setTitle("noteFx");
        primaryStage.getIcons().add(new Image(iconRes.toURI().toString()));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        FileMonitor.get().stopWatch();
        NoteFxThreadPool.getThreadPool().shutdownNow();
        NoteFxThreadPool.getFileMonitorPool().shutdownNow();
        main = null;
    }
}