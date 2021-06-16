package com.pyjava.daily.notification;

/**
 * <p>描述: 通知观察者 </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/16 16:24
 */
@FunctionalInterface
public interface NotificationObserver {
    /**
     * 通知更新
     *
     * @param messageName 消息名称
     * @param param       参数
     */
    void receivedNotification(String messageName, Object... param);
}

