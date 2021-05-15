package com.pyjava.daily.view.newdialog;

import com.pyjava.daily.model.Config;
import com.pyjava.daily.viewmodel.newdialog.NewDialogViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/14 17:54
 */
public class NewDialogView implements FxmlView<NewDialogViewModel>, Initializable {
    private static final Logger logger = LoggerFactory.getLogger(NewDialogView.class);
    @FXML
    public TextField dailyName;
    @FXML
    public TextField dailyLocation;
    @Inject
    private NotificationCenter notificationCenter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dailyName.setText("");
        this.dailyLocation.setText("");
    }

    public void openDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(new Stage());
        if (null == dir) {
            return;
        }
        this.dailyLocation.setText(dir.getAbsolutePath());
    }

    public void submit() {
        if ("".equals(this.dailyLocation.getText()) || "".equals(this.dailyName.getText())) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Cannot save settings");
            alert.setContentText("please enter daily name and daily location!");
            alert.getButtonTypes().add(new ButtonType("OK"));
            alert.showAndWait();
        } else {
            // 获取当前目录路径,判断该文件夹是否存在
            String filePath = this.dailyLocation.getText();
            File file = new File(filePath);
            if (file.exists()) {
                // 如果存在,则提示是否在该文件夹下创建daily工作空间
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("File Exists");
                alert.setContentText("Do you want to create Daily workspace in this directory?");
                alert.getButtonTypes().add(ButtonType.OK);
                alert.getButtonTypes().add(ButtonType.CANCEL);
                // 获取用户点击按钮
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == ButtonType.OK) {
                        logger.debug("file exists : click ok");
                        String dailyFilePath = file.getAbsolutePath()+ "\\.daily\\";
                        File dailyFile = new File(dailyFilePath);
                        if (!dailyFile.exists()) {
                            boolean mkdirs = dailyFile.mkdirs();
                            // TODO 创建项目文件
                        }
                        Config.setLastFilePath(this.dailyLocation.getText());
                        this.dailyName.setText("");
                        this.dailyLocation.setText("");
                        notificationCenter.publish("newDialog-submit");
                    } else if (result.get() == ButtonType.CANCEL) {
                        logger.debug("file exists : click cancel");
                    }
                }
            } else {
                // 如果不存在,则提示是否在创建该目录,并且创建daily工作空间
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("File Exists");
                alert.setContentText("Do you want to create Daily workspace in new directory?");
                alert.getButtonTypes().add(ButtonType.OK);
                alert.getButtonTypes().add(ButtonType.CANCEL);
                // 获取用户点击按钮
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == ButtonType.OK) {
                        logger.debug("file does not exist : click ok");
                        String dailyFilePath = file.getAbsolutePath()+ "\\.daily\\";
                        File dailyFile = new File(dailyFilePath);
                        boolean mkdirs = dailyFile.mkdirs();
                        // TODO 创建项目文件

                        Config.setLastFilePath(this.dailyLocation.getText());
                        this.dailyName.setText("");
                        this.dailyLocation.setText("");
                        notificationCenter.publish("newDialog-submit");
                    } else if (result.get() == ButtonType.CANCEL) {
                        logger.debug("file does not exist : click cancel");
                    }
                }
            }
        }
    }

    public void cancel() {
        this.dailyName.setText("");
        this.dailyLocation.setText("");
        notificationCenter.publish("newDialog-cancel");
    }
}
