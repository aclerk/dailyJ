package com.pyjava.daily;

import com.pyjava.daily.config.Config;
import com.pyjava.daily.thread.NoteFxThreadPool;
import com.pyjava.daily.view.main.MainView;
import com.pyjava.daily.viewmodel.main.MainViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.guice.MvvmfxGuiceApplication;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.prefs.Preferences;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/12 21:27
 */
public class Starter extends MvvmfxGuiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(Starter.class);

    private static Stage main;

    public static void main(String[] args) {
        Starter.launch(args);
    }

    @Override
    public void startMvvmfx(Stage stage) throws Exception {
        load();

        URL iconRes = getClass().getClassLoader().getResource("img/daily.png");
        assert iconRes != null;

        final ViewTuple<MainView, MainViewModel> tuple
                = FluentViewLoader.fxmlView(MainView.class).load();

        // Locate View for loaded FXML file
        final Parent view = tuple.getView();

        final Scene scene = new Scene(view, 1000, 618);
        stage.setTitle("daily");
        stage.getIcons().add(new Image(iconRes.toURI().toString()));
        stage.setScene(scene);
        main = stage;
        stage.show();
    }

    @Override
    public void stopMvvmfx() {
        NoteFxThreadPool.getThreadPool().shutdownNow();
        NoteFxThreadPool.getFileMonitorPool().shutdownNow();
        main = null;
    }

    public static Stage getMain() {
        return main;
    }

    private void load() throws Exception {
        Config.load(Preferences.userRoot().node("configs"));
    }
}
