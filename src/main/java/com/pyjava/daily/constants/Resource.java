package com.pyjava.daily.constants;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * <p>描述: 图标资源 </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/4/29 17:36
 */
public class Resource {
    public final static Image PEN_ICON = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("icon/pen-16.png")));
    public final static Image FOLDER_ICON = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("icon/folder-16.png")));
    public final static Image FOLDER_OPEN_ICON = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("icon/folder-open-16.png")));
    public final static Image FILE_ICON = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("icon/file-16.png")));
    public final static Image MAIN_ICON = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("icon/daily.ico")));
    public final static Image NOTE_ICON = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("icon/sticky-note-16.png")));
    public final static Image BOOK_ICON = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("icon/book-16.png")));
}
