package com.pyjava.daily.util;

import java.util.prefs.Preferences;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/14 11:34
 */
public class PreferenceUtil {
    public static void putPrefs(Preferences prefs, String key, String value, String def) {
        if (!value.equals(def)) {
            prefs.put(key, value);
        } else {
            prefs.remove(key);
        }
    }

    public static void putPrefsInt(Preferences prefs, String key, int value, int def) {
        if (value != def) {
            prefs.putInt(key, value);
        } else {
            prefs.remove(key);
        }
    }

    public static void putPrefsBoolean(Preferences prefs, String key, boolean value, boolean def) {
        if (value != def) {
            prefs.putBoolean(key, value);
        } else {
            prefs.remove(key);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T getPrefsEnum(Preferences prefs, String key, T def) {
        String s = prefs.get(key, null);
        if (s == null) {
            return def;
        }
        try {
            return (T) Enum.valueOf(def.getClass(), s);
        } catch (IllegalArgumentException ex) {
            return def;
        }
    }

    public static <T extends Enum<T>> void putPrefsEnum(Preferences prefs, String key, T value, T def) {
        if (value != def) {
            prefs.put(key, value.name());
        } else {
            prefs.remove(key);
        }
    }

}
