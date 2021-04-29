package com.pyjava.notefx.constants;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/4/29 17:36
 */
public class Resource {
    public final static Image PEN_ICON = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("icon/pen-16.png")));
}
