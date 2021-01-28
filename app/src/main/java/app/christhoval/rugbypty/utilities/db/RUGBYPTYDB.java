package app.christhoval.rugbypty.utilities.db;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.christhoval.rugbypty.models.Message;
import app.christhoval.rugbypty.models.Notification;
import app.christhoval.rugbypty.utilities.Debug;

/**
 * Created by christhoval on 03/24/15.
 */
public class RUGBYPTYDB extends DB {

    public static String TABLE_NOTIFICATION = "notification",
            NOTIFICATION_ID_COLUMN = "id",
            NOTIFICATION_TITLE_COLUMN = "title",
            NOTIFICATION_DESCRIPTION_COLUMN = "description",
            NOTIFICATION_TEAMHOME_COLUMN = "team_home",
            NOTIFICATION_TEAMHOMEFLAG_COLUMN = "team_home_flag",
            NOTIFICATION_TEAMHOMEPOINTS_COLUMN = "team_home_points",
            NOTIFICATION_TEAMAWAY_COLUMN = "team_away",
            NOTIFICATION_TEAMAWAYFLAG_COLUMN = "team_away_flag",
            NOTIFICATION_TEAMAWAYPOINTS_COLUMN = "team_away_points",
            NOTIFICATION_DATE_COLUMN = "match_date",
            NOTIFICATION_TIME_COLUMN = "match_time",
            NOTIFICATION_PLACE_COLUMN = "match_place",
            NOTIFICATION_PLAYED_COLUMN = "match_played";

    public static String TABLE_MESSAGE = "message",
            MESSAGE_ID_COLUMN = "id",
            MESSAGE_TYPE_COLUMN = "type",
            MESSAGE_USERNAME_COLUMN = "username",
            MESSAGE_MESSAGE_COLUMN = "message",
            MESSAGE_DATE_COLUMN = "date";

    public static String tablas[] = {TABLE_NOTIFICATION, TABLE_MESSAGE};

    public RUGBYPTYDB(Context context) {
        super(context);
    }

    public static String getSQLForTableFavoritos() {
        String table = "CREATE TABLE " + TABLE_NOTIFICATION + " (";
        table += NOTIFICATION_ID_COLUMN + "  TEXT not null,";
        table += NOTIFICATION_TITLE_COLUMN + " TEXT not null,";
        table += NOTIFICATION_DESCRIPTION_COLUMN + " TEXT,";
        table += NOTIFICATION_TEAMHOME_COLUMN + " TEXT not null,";
        table += NOTIFICATION_TEAMHOMEFLAG_COLUMN + " TEXT,";
        table += NOTIFICATION_TEAMHOMEPOINTS_COLUMN + " INTEGER DEFAULT 0,";
        table += NOTIFICATION_TEAMAWAY_COLUMN + " TEXT not null,";
        table += NOTIFICATION_TEAMAWAYFLAG_COLUMN + " TEXT,";
        table += NOTIFICATION_TEAMAWAYPOINTS_COLUMN + " INTEGER DEFAULT 0,";
        table += NOTIFICATION_DATE_COLUMN + " TEXT,";
        table += NOTIFICATION_TIME_COLUMN + " TEXT,";
        table += NOTIFICATION_PLACE_COLUMN + " TEXT,";
        table += NOTIFICATION_PLAYED_COLUMN + " INTEGER DEFAULT 0)";
        return table;
    }

    public static String getSQLForTableChat() {
        String table = "CREATE TABLE " + TABLE_MESSAGE + " (";
        table += MESSAGE_ID_COLUMN + "  TEXT not null,";
        table += MESSAGE_TYPE_COLUMN + " TEXT,";
        table += MESSAGE_USERNAME_COLUMN + " TEXT not null,";
        table += MESSAGE_MESSAGE_COLUMN + " TEXT not null,";
        table += MESSAGE_DATE_COLUMN + " TEXT not null)";
        return table;
    }


    public void clearCache() {
        this.borrarTabla(TABLE_NOTIFICATION);
        this.borrarTabla(TABLE_MESSAGE);
    }

    public boolean haveNotifications() {
        return have(TABLE_NOTIFICATION);
    }


    public boolean deleteNotificacions() {
        return borrarTabla(TABLE_NOTIFICATION);
    }

    public boolean deleteNotificationWithId(String notificationId) {
        return delete(TABLE_NOTIFICATION, String.format("%s=?", NOTIFICATION_ID_COLUMN), new String[]{notificationId});
    }

    public boolean setNotification(Notification notification) {
        return save(TABLE_NOTIFICATION, Notification.toJSon(notification));
    }

    /*
    public boolean updateNotification(Notification notification, String favoritoId) {
        return update(TABLE_NOTIFICATION, JSON.no(favorito, true), String.format("%s=?", NOTIFICATION_ID_COLUMN), new String[]{favoritoId});
    }
    */
    /*
    public JSONObject getFavoritoConId(String favoritoId) {
        return sql2JSONARRAY(String.format("SELECT * FROM %s Where %s=%s LIMIT 1;", TABLE_NOTIFICATION, NOTIFICATION_ID_COLUMN, favoritoId));
    }

    public JSONObject getFavoritoConPanapas(String panapass) {
        return sql2JSONARRAY(String.format("SELECT * FROM %s Where %s='%s' LIMIT 1;", TABLE_NOTIFICATION, NOTIFICATION_PANAPASS_COLUMN, panapass));
    }
    */
    public JSONObject getNotifications() {
        return getAll(TABLE_NOTIFICATION, Notification.NOTIFICATIONS);
    }

    public List<Notification> getNotificationsList() {
        List<Notification> l = new ArrayList<>();
        if (haveNotifications()) {
            try {
                JSONArray json = getNotifications().getJSONArray(Notification.NOTIFICATIONS);
                for (int i = 0; i < json.length(); i++) {
                    l.add(Notification.fromJSON(json.getJSONObject(i)));
                }
                Debug.i("Notifications: " + l.size());
                return l;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return l;
    }


    public boolean haveMessages() {
        return have(TABLE_MESSAGE);
    }


    public boolean haveMessageWithId(String value) {
        return exist(String.format("SELECT * FROM %s WHERE %s='%s' LIMIT 1;", TABLE_MESSAGE, MESSAGE_ID_COLUMN, value));
    }

    public boolean deleteMessages() {
        return borrarTabla(TABLE_MESSAGE);
    }

    public boolean deleteMessage(String messageId) {
        return delete(TABLE_MESSAGE, String.format("%s=?", MESSAGE_ID_COLUMN), new String[]{messageId});
    }

    public boolean setMessage(Message message) {
        return save(TABLE_MESSAGE, Message.toJSon(message));
    }

    public JSONObject getMessages() {
        return getAll(TABLE_MESSAGE, Message.MESSAGES);
    }

    public List<Message> getMessagesList() {
        List<Message> l = new ArrayList<>();
        if (haveMessages()) {
            try {
                JSONArray json = getMessages().getJSONArray(Notification.NOTIFICATIONS);
                for (int i = 0; i < json.length(); i++) {
                    l.add(Message.fromJSON(json.getJSONObject(i)));
                }
                Debug.i("Chats: " + l.size());
                return l;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return l;
    }
}
