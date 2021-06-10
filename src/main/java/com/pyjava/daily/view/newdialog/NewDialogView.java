package com.pyjava.daily.view.newdialog;

import com.pyjava.daily.constants.Constants;
import com.pyjava.daily.view.menu.MenuView;
import com.pyjava.daily.viewmodel.NewDialogViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
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

    }

    public void openDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(MenuView.stage);
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
            String dailyName = this.dailyName.getText();
            String file = filePath + Constants.FILE_SEPARATOR + dailyName + Constants.DB_SUFFIX;
            notificationCenter.publish("newDialog-submit", file);
        }
    }

    public void cancel() {
        this.dailyName.setText("");
        this.dailyLocation.setText("");
        notificationCenter.publish("newDialog-cancel");
    }
}
