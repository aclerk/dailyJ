package com.pyjava.daily.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pyjava.daily.Starter;
import com.pyjava.daily.config.GlobalConfig;
import com.pyjava.daily.constants.Constants;
import com.pyjava.daily.constants.Resource;
import com.pyjava.daily.notification.NotificationCenter;
import com.pyjava.daily.util.InjectorUtils;
import com.pyjava.daily.util.JdbcUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
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

    public void createDaily() {
        Stage stage = new Stage();

        // 生成弹窗
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        Label nameLabel = new Label("daily name");
        gridPane.add(nameLabel, 0, 1);
        TextField nameField = new TextField();
        nameField.setPromptText("please enter daily name");
        gridPane.add(nameField, 1, 1);
        GridPane.setHgrow(nameField, Priority.ALWAYS);

        Label locationLabel = new Label("daily location");
        gridPane.add(locationLabel, 0, 2);

        TextField locationField = new TextField();
        locationField.setPromptText("please enter daily location");
        gridPane.add(locationField, 1, 2);
        GridPane.setHgrow(locationField, Priority.ALWAYS);

        Button button = new Button("...");

        button.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            File dir = chooser.showDialog(Starter.getMain());
            if (null == dir) {
                return;
            }
            locationField.setText(dir.getAbsolutePath());
        });
        gridPane.add(button, 9, 2);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(0, 10, 10, 0));
        Button submit = new Button("确定");
        submit.setOnAction(event -> {
            logger.debug("submit");
            if ("".equals(nameField.getText()) || "".equals(locationField.getText())) {
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("Cannot save settings");
                alert.setContentText("please enter daily name and daily location!");
                alert.getButtonTypes().add(new ButtonType("OK"));
                alert.showAndWait();
            } else {
                // 获取当前目录路径,判断该文件夹是否存在
                String filePath = locationField.getText();
                String dailyName = nameField.getText();
                String db = filePath + Constants.FILE_SEPARATOR + dailyName + Constants.DB_SUFFIX;

                // GlobalConfig中加入
                Injector injector = InjectorUtils.getInjector();
                GlobalConfig instance = injector.getInstance(GlobalConfig.class);
                List<String> dbs = instance.getDbs();
                boolean flag = false;
                if (CollectionUtils.isNotEmpty(dbs)) {
                    for (String s : dbs) {
                        if (s.equals(db)) {
                            flag = true;
                            break;
                        }
                    }
                }

                if (!flag) {
                    dbs.add(db);
                    // 入文件
                    try {
                        File globalConfigFile = new File(Constants.GLOBAL_CONFIG_FILE_PATH);
                        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(globalConfigFile), StandardCharsets.UTF_8);
                        osw.write(Starter.OBJECT_MAPPER.writeValueAsString(instance));
                        //清空缓冲区，强制输出数据
                        osw.flush();
                        //关闭输出流
                        osw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        JdbcUtil.initDb(db);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Alert alert = new Alert(Alert.AlertType.NONE);
                    alert.setTitle("Information");
                    alert.setContentText("would you like to open this daily file in current window");
                    alert.getButtonTypes().addAll(
                            new ButtonType("Yes", ButtonBar.ButtonData.YES),
                            new ButtonType("No", ButtonBar.ButtonData.NO)
                    );
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.isPresent()) {
                        ButtonType bt = buttonType.get();
                        if(bt.getButtonData().equals(ButtonBar.ButtonData.YES)){
                            // TODO 打开
                            logger.debug("open the daily project");
                        }else if(bt.getButtonData().equals(ButtonBar.ButtonData.NO)){
                            logger.debug("do not open the daily project");
                        }
                    }
                }
                stage.close();
            }
        });
        Button cancel = new Button("取消");
        cancel.setOnAction(event -> {
            logger.debug("cancel");
            stage.close();
        });
        hBox.getChildren().addAll(submit, cancel);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        borderPane.setBottom(hBox);

        Scene scene = new Scene(borderPane, 600, 378);
        stage.setTitle("new daily");
        stage.getIcons().add(Resource.MAIN_ICON);
        stage.initOwner(Starter.getMain());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    public void openDaily() {

    }

    public void exit() {
        notificationCenter.publish("exit");
    }
}
