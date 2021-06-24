package com.pyjava.daily;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pyjava.daily.config.DailyModule;
import com.pyjava.daily.constants.Constants;
import com.pyjava.daily.thread.NoteFxThreadPool;
import com.pyjava.daily.util.InjectorUtils;
import com.pyjava.daily.util.JdbcUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static com.pyjava.daily.constants.Constants.DB_PATH;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/12 21:27
 */
public class Starter extends Application {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(Starter.class);

    private static Stage main;

    public static void main(String[] args) {
        Starter.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Injector injector = Guice.createInjector(new DailyModule());
        InjectorUtils.setInjector(injector);
        load();
        main = primaryStage;

        URL iconRes = getClass().getClassLoader().getResource("img/daily.png");
        assert iconRes != null;
        URL resource = getClass().getClassLoader().getResource("com/pyjava/daily/controller/main.fxml");
        assert resource != null;

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(injector::getInstance);
        loader.setLocation(resource);
        Scene scene;
        try {
            Parent root = loader.load();
            scene = new Scene(root, 1000, 618);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }


        primaryStage.setTitle("daily");
        primaryStage.getIcons().add(new Image(iconRes.toURI().toString()));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        logger.debug("daily shutdown now");
        NoteFxThreadPool.getThreadPool().shutdownNow();
        main = null;
    }

    /**
     * 全局配置文件创建/读取
     *
     * @throws IOException IO异常
     */
    private void load() throws Exception {
        // 全局配置文件创建
        File globalConfigFolder = new File(Constants.GLOBAL_CONFIG_FOLDER_PATH);
        if (!globalConfigFolder.exists()) {
            boolean mkdir = globalConfigFolder.mkdir();
        }
        File dbFile = new File(DB_PATH);
        JdbcUtil.init(DB_PATH);

        if (!dbFile.exists()) {
            // 创建完成dailyJ目录后,获取jdbc连接
            JdbcUtil.initDb();
        }
    }

    public static Stage getMain() {
        return main;
    }
}
