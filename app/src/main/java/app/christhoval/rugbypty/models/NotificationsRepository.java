package app.christhoval.rugbypty.models;

import android.support.v4.util.ArrayMap;

import java.util.ArrayList;

import app.christhoval.rugbypty.utilities.db.RUGBYPTYDB;

/**
 * Repositorio de push notifications
 */
public final class NotificationsRepository {

    private static ArrayMap<String, Notification> LOCAL_PUSH_NOTIFICATIONS = new ArrayMap<>();
    private static NotificationsRepository INSTANCE;

    private NotificationsRepository() {
    }

    public static NotificationsRepository getInstance() {
        if (INSTANCE == null) {
            return new NotificationsRepository();
        } else {
            return INSTANCE;
        }
    }

    public void getPushNotifications(RUGBYPTYDB db, LoadCallback callback) {
        callback.onLoaded(new ArrayList<>(getLocalPushNotifications(db).values()));
    }

    public void savePushNotification(Notification notification) {
        LOCAL_PUSH_NOTIFICATIONS.put(notification.getId(), notification);
    }


    private ArrayMap<String, Notification> getLocalPushNotifications(RUGBYPTYDB db) {
        for (Notification notification : db.getNotificationsList()) {
            if (!LOCAL_PUSH_NOTIFICATIONS.containsKey(notification.getId())) {
                LOCAL_PUSH_NOTIFICATIONS.put(notification.getId(), notification);
            }
        }
        return LOCAL_PUSH_NOTIFICATIONS;
    }

    public interface LoadCallback {
        void onLoaded(ArrayList<Notification> notifications);
    }

}
