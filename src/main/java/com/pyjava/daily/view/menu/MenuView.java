package com.pyjava.daily.view.menu;

import com.google.inject.Injector;
import com.pyjava.daily.Starter;
import com.pyjava.daily.config.GlobalConfig;
import com.pyjava.daily.constants.Constants;
import com.pyjava.daily.constants.Resource;
import com.pyjava.daily.util.InjectorUtils;
import com.pyjava.daily.view.newdialog.NewDialogView;
import com.pyjava.daily.viewmodel.menu.MenuViewModel;
import com.pyjava.daily.viewmodel.newdialog.NewDialogViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    public static Stage stage = null;
    public static Scene scene = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notificationCenter.subscribe("newDialog-submit", (key, payload) -> {
            logger.debug("key={},payload={}", key, payload);
            stage.close();
            stage = null;
            scene = null;
            String db = payload[0].toString();

            // GlobalConfig中加入
            Injector injector = InjectorUtils.getInjector();
            GlobalConfig instance = injector.getInstance(GlobalConfig.class);
            instance.setLastOpenDb(db);
            instance.getDbs().add(db);
            // 入文件
            try{
                File globalConfigFile = new File(Constants.GLOBAL_CONFIG_FILE_PATH);
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(globalConfigFile), StandardCharsets.UTF_8);
                osw.write(Starter.OBJECT_MAPPER.writeValueAsString(instance));
                //清空缓冲区，强制输出数据
                osw.flush();
                //关闭输出流
                osw.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        });
        notificationCenter.subscribe("newDialog-cancel", (key, payload) -> {
            logger.debug("key={},payload={}", key, payload);
            stage.close();
            stage = null;
            scene = null;
        });
    }

    public void createDaily() {

        final ViewTuple<NewDialogView, NewDialogViewModel> tuple
                = FluentViewLoader.fxmlView(NewDialogView.class).load();

        // Locate View for loaded FXML file
        final Parent view = tuple.getView();

        scene = new Scene(view, 600, 378);
        stage = new Stage();
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
