package com.pyjava.daily.notification;

/**
 * <p>描述: 通知中心 </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/16 16:28
 */
public interface NotificationCenter {
    /**
     * 订阅
     *
     * @param messageName 消息名称
     * @param observer    {@link FunctionalInterface} 更新操作
     */
    void subscribe(String messageName, NotificationObserver observer);

    /**
     * 发布
     *
     * @param messageName 消息名称
     * @param payload     载荷
     */
    void publish(String messageName, Object... payload);
}
