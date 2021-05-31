package com.pyjava.daily.util;

import com.google.inject.Injector;

import java.util.Objects;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/1 0:09
 */
public class InjectorUtils {
    private static Injector injector;

    /**
     * set Injector
     *
     * @param injector Injector
     */
    public static void setInjector(Injector injector) {
        InjectorUtils.injector = injector;
    }

    /**
     * 获取上下文的 Injector 对象，方便获取上下文
     *
     * @return Injector
     */
    public static Injector getInjector() {
        if (Objects.isNull(injector)) {
            throw new NullPointerException("Injector is null");
        }
        return injector;
    }

    /**
     * 获取对象
     *
     * @param tClass 目标 class
     * @param <T>    目标对象Class
     * @return 目标对象
     */
    public static <T> T getInstance(Class<T> tClass) {
        return getInjector().getInstance(tClass);
    }
}
