package com.pyjava.daily.notification;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>描述: 默认通知中心 </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/6/16 16:29
 */
@Singleton
public class DefaultNotificationCenter implements NotificationCenter {
    private static final Logger logger = LoggerFactory.getLogger(DefaultNotificationCenter.class);

    private final ObserverMap globalObservers = new ObserverMap();

    public DefaultNotificationCenter() {
        logger.debug("DefaultNotificationCenter is initializing");
    }

    @Override
    public void subscribe(String messageName, NotificationObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("The observer must not be null.");
        }
        addObserver(messageName, observer, this.globalObservers);
    }

    @Override
    public void publish(String messageName, Object... payload) {
        publish(messageName, payload, this.globalObservers);
    }

    private static void publish(String messageName, Object[] payload, ObserverMap observerMap) {
        Collection<NotificationObserver> notificationReceivers = observerMap.get(messageName);
        if (notificationReceivers != null) {
            for (NotificationObserver observer : notificationReceivers) {
                observer.receivedNotification(messageName, payload);
            }
        }

    }

    private static void addObserver(String messageName, NotificationObserver observer, ObserverMap observerMap) {
        if (!observerMap.containsKey(messageName)) {
            observerMap.put(messageName, new CopyOnWriteArrayList<>());
        }
        List<NotificationObserver> observers = observerMap.get(messageName);
        if (observers.contains(observer)) {
            logger.warn("Subscribe the observer [" + observer + "] for the message [" + messageName + "], but the same observer was already added for this message in the past.");
        }
        observers.add(observer);
    }

    private class ObserverMap extends HashMap<String, List<NotificationObserver>> {
        private ObserverMap() {
        }
    }
}
