package app.christhoval.rugbypty.mvp.notifications;


import java.util.ArrayList;

import app.christhoval.rugbypty.models.Notification;
import app.christhoval.rugbypty.mvp.BasePresenter;
import app.christhoval.rugbypty.mvp.BaseView;

/**
 * Interacción MVP en Notificaciones
 */
public interface NotificationContract {

    interface View extends BaseView<Presenter> {

        void showNotifications(ArrayList<Notification> notifications);

        void showEmptyState(boolean empty);

        void popPushNotification(Notification pushMessage);
    }

    interface Presenter extends BasePresenter {

        void registerAppClient();

        void loadNotifications();

        void savePushMessage(Notification pushMessage);
    }
}
