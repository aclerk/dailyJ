package com.pyjava.daily.model;

import com.pyjava.daily.util.PreferenceUtil;
import javafx.beans.property.SimpleStringProperty;

import java.util.function.Function;
import java.util.prefs.Preferences;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/14 11:33
 */
public class ConfigStringProperty extends SimpleStringProperty {
    public ConfigStringProperty() {
    }

    public ConfigStringProperty(Preferences prefs, String key, String def) {
        init(prefs, key, def);
    }

    public void init(Preferences prefs, String key, String def) {
        init(prefs, key, def, value -> value);
    }

    public void init(Preferences prefs, String key, String def, Function<String, String> loadConverter) {
        set(loadConverter.apply(prefs.get(key, def)));
        addListener((ob, o, n) -> PreferenceUtil.putPrefs(prefs, key, get(), def));
    }
}
