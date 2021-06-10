package com.pyjava.daily.view.footer;

import com.pyjava.daily.viewmodel.FooterViewModel;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * <p>描述: 底部视图 </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/12 23:46
 */
public class FooterView implements FxmlView<FooterViewModel>, Initializable {
    @FXML
    public StackPane footerViewPane;
    @FXML
    public Label textType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
