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
import com.pyjava.daily.view.main.MainView;
import com.pyjava.daily.viewmodel.MainViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.guice.MvvmfxGuiceApplication;
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
public class Starter extends MvvmfxGuiceApplication {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
        main = null;
        logger.debug("daily exit");
    }

    /**
     * 全局配置文件创建/读取
     * @throws IOException IO异常
     */
    private void load() throws IOException {
        // 全局配置文件创建
        File globalConfigFolder = new File(Constants.GLOBAL_CONFIG_FOLDER_PATH);
        if (!globalConfigFolder.exists()) {
            boolean mkdir = globalConfigFolder.mkdir();
        }
        Injector injector = Guice.createInjector(new DailyModule());
        InjectorUtils.setInjector(injector);
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
        if(StringUtils.isNotEmpty(instance.getLastOpenDb())){
            JdbcUtil.init(instance.getLastOpenDb());
        }
    }

    public static Stage getMain() {
        return main;
    }
}
