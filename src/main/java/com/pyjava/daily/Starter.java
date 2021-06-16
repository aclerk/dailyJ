package com.pyjava.daily;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pyjava.daily.config.DailyModule;
import com.pyjava.daily.config.GlobalConfig;
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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
    private void load() throws IOException {
        // 全局配置文件创建
        File globalConfigFolder = new File(Constants.GLOBAL_CONFIG_FOLDER_PATH);
        if (!globalConfigFolder.exists()) {
            boolean mkdir = globalConfigFolder.mkdir();
        }
        Injector injector = InjectorUtils.getInjector();

        GlobalConfig instance = injector.getInstance(GlobalConfig.class);
        File globalConfigFile = new File(Constants.GLOBAL_CONFIG_FILE_PATH);
        if (!globalConfigFile.exists()) {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(globalConfigFile), StandardCharsets.UTF_8);
            osw.write(OBJECT_MAPPER.writeValueAsString(instance));
            //清空缓冲区，强制输出数据
            osw.flush();
            //关闭输出流
            osw.close();
        } else {
            // 如果已经存在了该文件则直接读取
            FileReader fileReader = new FileReader(globalConfigFile);
            Reader reader = new InputStreamReader(new FileInputStream(globalConfigFile), StandardCharsets.UTF_8);
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            String globalConfigStr = sb.toString();
            GlobalConfig globalConfig = OBJECT_MAPPER.readValue(globalConfigStr, GlobalConfig.class);
            instance.setGlobalConfig(globalConfig);
        }
        logger.debug(instance.toString());
        // 初始化datasource
        if (StringUtils.isNotEmpty(instance.getLastOpenDb())) {
            JdbcUtil.init(instance.getLastOpenDb());
        }
    }

    public static Stage getMain() {
        return main;
    }
}
