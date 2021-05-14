package com.pyjava.daily.view.menu;

import com.pyjava.daily.model.Config;
import com.pyjava.daily.viewmodel.menu.MenuViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * <p>描述: 菜单视图 </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/12 23:07
 */
public class MenuView implements FxmlView<MenuViewModel>, Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MenuView.class);

    @FXML
    private BorderPane menuViewPane;

    @InjectViewModel
    private MenuViewModel menuViewModel;

    @Inject
    private NotificationCenter notificationCenter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void createDaily() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择daily目录");
        File file = chooser.showDialog(new Stage());
        if(null == file){
            return;
        }
        Config.setLastFilePath(file.getAbsolutePath());
        logger.info("last file path, {}", file.getAbsolutePath());

    }

    public void openDaily() {
    }

    public void exit() {
        notificationCenter.publish("exit");
    }
}
