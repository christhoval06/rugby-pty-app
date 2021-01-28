package app.christhoval.rugbypty.mvp.notifications;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import java.util.ArrayList;

import app.christhoval.rugbypty.RugbyPTY;
import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.models.Notification;
import app.christhoval.rugbypty.utilities.db.RUGBYPTYDB;

/**
 * Muestra lista de notificaciones
 */
public class NotificationsFragment extends Fragment implements NotificationContract.View {

    public static final String DEEP_LINK = "notifications";
    public static final String ACTION_NOTIFY_NEW_MATCH = "NOTIFY_NEW_MATCH";
    private BroadcastReceiver mNotificationsReceiver;

    private RecyclerView mRecyclerView;
    private LinearLayout mNoMessagesView;
    private NotificationsAdapter mNotificatiosAdapter;

    private NotificationsPresenter mPresenter;

    private RUGBYPTYDB db;


    public static NotificationsFragment newInstance(Bundle extras) {
        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    public NotificationsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RugbyPTY rugbyPTY = (RugbyPTY) getActivity().getApplication();
        if (getArguments() != null) {
            // Gets de argumentos
        }
        db = rugbyPTY.getDb();

        mNotificationsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.savePushMessage(Notification.fromIntent(intent));
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        mNotificatiosAdapter = new NotificationsAdapter();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_notifications_list);
        mNoMessagesView = (LinearLayout) root.findViewById(R.id.noMessages);
        mRecyclerView.setAdapter(mNotificatiosAdapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mNotificationsReceiver, new IntentFilter(ACTION_NOTIFY_NEW_MATCH));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(mNotificationsReceiver);
    }

    @Override
    public void showNotifications(ArrayList<Notification> notifications) {
        mNotificatiosAdapter.replaceData(notifications);
    }

    @Override
    public void showEmptyState(boolean empty) {
        mRecyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        mNoMessagesView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void popPushNotification(Notification pushMessage) {
        mNotificatiosAdapter.addItem(pushMessage);
    }

    @Override
    public void setPresenter(NotificationContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = (NotificationsPresenter) presenter;
        } else {
            throw new RuntimeException("El presenter de notificaciones no puede ser null");
        }
    }

    public RUGBYPTYDB getDb() {
        return db;
    }
}
