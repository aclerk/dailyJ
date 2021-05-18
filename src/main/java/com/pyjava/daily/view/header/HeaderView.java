package com.pyjava.daily.view.header;

import com.pyjava.daily.model.Config;
import com.pyjava.daily.viewmodel.header.HeaderViewModel;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.apache.commons.lang.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/18 23:47
 */
public class HeaderView implements FxmlView<HeaderViewModel>, Initializable {
    @FXML
    private BorderPane headerPane;

    @FXML
    public Label workspace;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String lastFilePath = Config.getLastFilePath();
        if(StringUtils.isNotEmpty(lastFilePath)){
            // 说明曾经打开过,直接打开
            workspace.setText(lastFilePath);
        }else{
            // 还没有选择工作空间的时候,把分割符放到最左边展示项目文件介绍
            workspace.setText("还未打开工作空间");
        }
    }
}
