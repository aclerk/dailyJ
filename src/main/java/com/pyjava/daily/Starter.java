package com.pyjava.daily;

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

import java.net.URL;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/12 21:27
 */
public class Starter extends MvvmfxGuiceApplication {

    private static Stage main;

    public static void main(String[] args) {
        Starter.launch(args);
    }

    @Override
    public void startMvvmfx(Stage stage) throws Exception {

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
        main = null;
    }

    public static Stage getMain() {
        return main;
    }
}
