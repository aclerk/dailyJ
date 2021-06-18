package com.pyjava.daily.controller;

import com.google.inject.Inject;
import com.pyjava.daily.entity.Notebook;
import com.pyjava.daily.notification.NotificationCenter;
import com.pyjava.daily.service.NoteService;
import com.pyjava.daily.util.TimeUtils;
import com.pyjava.daily.util.UuidUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.pyjava.daily.constants.Resource.BOOK_ICON;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/9 22:07
 */
public class NoteController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
    @FXML
    public StackPane notePane;
    @FXML
    public SplitPane splitPane;
    @FXML
    public TreeView<Notebook> leftTree;
    @FXML
    public StackPane areaPane;

    @Inject
    private NotificationCenter notificationCenter;

    @Inject
    private NoteService noteService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        splitPane.setDividerPosition(0, 0.2);
        splitPane.setDividerPosition(1, 0.4);
        notePane.widthProperty().addListener((observableValue, number, t1) -> splitPane.setDividerPosition(0, 0.2));

        notificationCenter.subscribe("notebook-update", (key, payload) -> {
            logger.debug("key={},payload={}", key, payload);
            List<Notebook> notebooks = noteService.list();
            TreeItem<Notebook> items = noteService.buildTree(notebooks);
            leftTree.setRoot(items);
            leftTree.setShowRoot(false);
        });

        leftTree.setCellFactory(new Callback<>() {
            @Override
            public TreeCell<Notebook> call(TreeView<Notebook> tv) {
                return new TreeCell<>() {
                    @Override
                    protected void updateItem(Notebook item, boolean empty) {
                        super.updateItem(item, empty);
                        ImageView bookIcon = new ImageView();
                        bookIcon.setImage(BOOK_ICON);
                        bookIcon.setFitWidth(16);
                        bookIcon.setFitHeight(16);
                        Node graphic = item == null ? null : bookIcon;
                        if(null != graphic){
                            setGraphic(bookIcon);
                        }else{
                            setGraphic(null);
                        }
                        setText((empty || item == null) ? "" : item.getName());
                    }
                };
            }
        });
        leftTree.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            //这里点击的如果是树，则event.getTarget() 的类型是 LabeledText,可以根据这个特点做判断
            Node node = event.getPickResult().getIntersectedNode();
            if (node instanceof TreeCell && "".equals(((TreeCell) node).getText())) {
                logger.debug("clear selection");
                leftTree.getSelectionModel().clearSelection();
            }
        });
        // 目录树中添加菜单那
        ContextMenu menu = new ContextMenu();
        MenuItem addNoteBook = new MenuItem("新建笔记本");
        addNoteBook.setOnAction(actionEvent -> {
            TreeItem<Notebook> selectItem = leftTree.getSelectionModel().getSelectedItem();
            logger.debug("add notebook in {}", selectItem == null ? "root" : selectItem.getValue());

            // 输入notebook名称
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Text Input Dialog");

            ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));
            TextField notebookName = new TextField();
            notebookName.setPromptText("notebook name");
            dialog.getDialogPane().setContent(gridPane);
            gridPane.add(notebookName, 1, 0);
            gridPane.add(new Label("notebook'name:"), 0, 0);
            Platform.runLater(notebookName::requestFocus);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == submitButtonType) {
                    String text = notebookName.getText();
                    if(text == null || "".equals(text)){
                        return null;
                    }else{
                        return text;
                    }
                }
                return null;
            });

            Optional<String> s = dialog.showAndWait();
            if (s.isPresent()) {
                String parentId = null;
                if(selectItem != null){
                    Notebook notebook = selectItem.getValue();
                    parentId = notebook.getNotebookId();
                }
                Notebook saveEntity = new Notebook();
                Date date = new Date();
                saveEntity.setNotebookId(TimeUtils.getTimeString(date)+ UuidUtils.getUuid());
                saveEntity.setParentId(parentId);
                saveEntity.setName(s.get());
                saveEntity.setCreateTime(date);
                saveEntity.setUpdateTime(date);
                noteService.save(saveEntity);
                notificationCenter.publish("notebook-update");
            }
        });
        MenuItem deleteNoteBook = new MenuItem("删除");
        deleteNoteBook.setOnAction(actionEvent -> {
            TreeItem<Notebook> selectItem = leftTree.getSelectionModel().getSelectedItem();
            logger.debug("delete notebook in {}", selectItem == null ? "root" : selectItem.getValue());

            if(selectItem != null){
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("Information");
                alert.setContentText("Would you like to delete this notebook?");
                alert.getButtonTypes().addAll(
                        new ButtonType("Yes", ButtonBar.ButtonData.YES),
                        new ButtonType("No", ButtonBar.ButtonData.NO)
                );
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.isPresent()) {
                    ButtonType bt = buttonType.get();
                    if(bt.getButtonData().equals(ButtonBar.ButtonData.YES)){
                        logger.debug("delete the notebook");
                        Notebook notebook = selectItem.getValue();
                        noteService.deleteById(notebook.getNotebookId());
                        notificationCenter.publish("notebook-update");
                    }else if(bt.getButtonData().equals(ButtonBar.ButtonData.NO)){
                        logger.debug("do not delete the notebook");
                    }
                }
            }
        });
        menu.getItems().addAll(addNoteBook, deleteNoteBook);
        leftTree.setContextMenu(menu);
        notificationCenter.publish("notebook-update");
    }
}
