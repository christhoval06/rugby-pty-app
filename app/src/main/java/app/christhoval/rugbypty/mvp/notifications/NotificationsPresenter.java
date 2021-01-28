package app.christhoval.rugbypty.mvp.notifications;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import app.christhoval.rugbypty.models.Notification;
import app.christhoval.rugbypty.models.NotificationsRepository;
import app.christhoval.rugbypty.utilities.db.RUGBYPTYDB;

/**
 * Presentador de las notificaciones
 */
public class NotificationsPresenter implements NotificationContract.Presenter {
    private final NotificationContract.View mNotificationView;
    private final FirebaseMessaging mFCMInteractor;
    private RUGBYPTYDB db;

    public NotificationsPresenter(NotificationContract.View notificationView, FirebaseMessaging FCMInteractor, RUGBYPTYDB db) {

        mNotificationView = notificationView;
        mFCMInteractor = FCMInteractor;

        this.db = db;

        notificationView.setPresenter(this);
    }

    @Override
    public void start() {
        registerAppClient();
        loadNotifications();
    }

    @Override
    public void registerAppClient() {
        mFCMInteractor.subscribeToTopic("games");
    }

    @Override
    public void loadNotifications() {
        NotificationsRepository.getInstance().getPushNotifications(db,
                new NotificationsRepository.LoadCallback() {
                    @Override
                    public void onLoaded(ArrayList<Notification> notifications) {
                        if (notifications.size() > 0) {
                            mNotificationView.showEmptyState(false);
                            mNotificationView.showNotifications(notifications);
                        } else {
                            mNotificationView.showEmptyState(true);
                        }
                    }
                }
        );
    }

    @Override
    public void savePushMessage(Notification pushMessage) {
        NotificationsRepository.getInstance().savePushNotification(pushMessage);

        mNotificationView.showEmptyState(false);
        mNotificationView.popPushNotification(pushMessage);
    }

}
