package app.christhoval.rugbypty.models;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import app.christhoval.rugbypty.utilities.Debug;
import app.christhoval.rugbypty.utilities.db.RUGBYPTYDB;

/**
 * Representación de una promoción en forma de push notification
 */
public class Notification {
    public static String NOTIFICATIONS = "notifications";
    private String id;
    private String mTitle;
    private String mDescription;
    private String mTeamHomeName;
    private String mTeamHomeFlag;
    private int mTeamHomePoints;
    private String mTeamAwayName;
    private String mTeamAwayFlag;
    private int mTeamAwayPoints;
    private String mDate;
    private String mTime;
    private String mPlace;
    private int mPoints;
    private boolean mWasPlayed;

    public Notification() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getDate() {
        return mDate;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getTime() {
        return mTime;
    }

    public void setPoints(int points) {
        mPoints = points;
    }

    public int getPoints() {
        return mPoints;
    }

    public void setWasPlayed(String wasPlayed) {
        mWasPlayed = Boolean.parseBoolean(wasPlayed);
    }

    public boolean isWasPlayed() {
        return mWasPlayed;
    }

    /***
     * TEAM HOME
     ***/
    public void setTeamHomeName(String mTeamHomeName) {
        this.mTeamHomeName = mTeamHomeName;
    }

    public String getTeamHomeName() {
        return mTeamHomeName;
    }

    public void setTeamHomeFlag(String mTeamHomeFlag) {
        this.mTeamHomeFlag = mTeamHomeFlag;
    }

    public String getTeamHomeFlag() {
        return mTeamHomeFlag;
    }

    public void setTeamHomePoints(int points) {
        this.mTeamHomePoints = points;
    }

    public int getTeamHomePoints() {
        return mTeamHomePoints;
    }

    /***
     * TEAM AWAY
     ***/
    public void setTeamAwayName(String mTeamAwayName) {
        this.mTeamAwayName = mTeamAwayName;
    }

    public String getTeamAwayName() {
        return mTeamAwayName;
    }

    public void setTeamAwayFlag(String mTeamAwayFlag) {
        this.mTeamAwayFlag = mTeamAwayFlag;
    }

    public String getTeamAwayFlag() {
        return mTeamAwayFlag;
    }

    public void setTeamAwayPoints(int points) {
        this.mTeamAwayPoints = points;
    }

    public int getTeamAwayPoints() {
        return mTeamAwayPoints;
    }

    public void setPlace(String mPlace) {
        this.mPlace = mPlace;
    }

    public String getPlace() {
        return mPlace;
    }



    public static Notification fromJSON(JSONObject json) {
        Notification notification = new Notification();
        try {
            notification.setId(json.getString(RUGBYPTYDB.NOTIFICATION_ID_COLUMN));
            notification.setTitle(json.getString(RUGBYPTYDB.NOTIFICATION_TITLE_COLUMN));
            notification.setDescription(json.getString(RUGBYPTYDB.NOTIFICATION_DESCRIPTION_COLUMN));

            notification.setTeamHomeName(json.getString(RUGBYPTYDB.NOTIFICATION_TEAMHOME_COLUMN));
            notification.setTeamHomeFlag(json.getString(RUGBYPTYDB.NOTIFICATION_TEAMHOMEFLAG_COLUMN));
            notification.setTeamHomePoints(json.getInt(RUGBYPTYDB.NOTIFICATION_TEAMHOMEPOINTS_COLUMN));

            notification.setTeamAwayName(json.getString(RUGBYPTYDB.NOTIFICATION_TEAMAWAY_COLUMN));
            notification.setTeamAwayFlag(json.getString(RUGBYPTYDB.NOTIFICATION_TEAMAWAYFLAG_COLUMN));
            notification.setTeamAwayPoints(json.getInt(RUGBYPTYDB.NOTIFICATION_TEAMAWAYPOINTS_COLUMN));

            notification.setDate(json.getString(RUGBYPTYDB.NOTIFICATION_DATE_COLUMN));
            notification.setTime(json.getString(RUGBYPTYDB.NOTIFICATION_TIME_COLUMN));
            notification.setPlace(json.getString(RUGBYPTYDB.NOTIFICATION_PLACE_COLUMN));
            notification.setWasPlayed(json.getString(RUGBYPTYDB.NOTIFICATION_PLAYED_COLUMN));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return notification;
    }

    public static Notification fromIntent(Intent intent) {
        Notification notification = new Notification();
        notification.setTitle(intent.getStringExtra(RUGBYPTYDB.NOTIFICATION_TITLE_COLUMN));
        notification.setDescription(intent.getStringExtra(RUGBYPTYDB.NOTIFICATION_DESCRIPTION_COLUMN));

        notification.setTeamHomeName(intent.getStringExtra(RUGBYPTYDB.NOTIFICATION_TEAMHOME_COLUMN));
        notification.setTeamHomeFlag(intent.getStringExtra(RUGBYPTYDB.NOTIFICATION_TEAMHOMEFLAG_COLUMN));
        notification.setTeamHomePoints(Integer.parseInt(intent.getStringExtra(RUGBYPTYDB.NOTIFICATION_TEAMHOMEPOINTS_COLUMN)));

        notification.setTeamAwayName(intent.getStringExtra(RUGBYPTYDB.NOTIFICATION_TEAMAWAY_COLUMN));
        notification.setTeamAwayFlag(intent.getStringExtra(RUGBYPTYDB.NOTIFICATION_TEAMAWAYFLAG_COLUMN));
        notification.setTeamAwayPoints(Integer.parseInt(intent.getStringExtra(RUGBYPTYDB.NOTIFICATION_TEAMAWAYPOINTS_COLUMN)));

        notification.setDate(intent.getStringExtra(RUGBYPTYDB.NOTIFICATION_DATE_COLUMN));
        notification.setTime(intent.getStringExtra(RUGBYPTYDB.NOTIFICATION_TIME_COLUMN));
        notification.setPlace(intent.getStringExtra(RUGBYPTYDB.NOTIFICATION_PLACE_COLUMN));
        notification.setWasPlayed(String.valueOf(Integer.parseInt(intent.getStringExtra(RUGBYPTYDB.NOTIFICATION_PLAYED_COLUMN)) == 1));
        return notification;
    }


    public static JSONObject toJSon(Notification notification) {
        try {
            JSONObject json = new JSONObject();
            json.put(RUGBYPTYDB.NOTIFICATION_ID_COLUMN, notification.getId());
            json.put(RUGBYPTYDB.NOTIFICATION_TITLE_COLUMN, notification.getTitle());
            json.put(RUGBYPTYDB.NOTIFICATION_DESCRIPTION_COLUMN, notification.getDescription());

            json.put(RUGBYPTYDB.NOTIFICATION_TEAMHOME_COLUMN, notification.getTeamHomeName());
            json.put(RUGBYPTYDB.NOTIFICATION_TEAMHOMEFLAG_COLUMN, notification.getTeamHomeFlag());
            json.put(RUGBYPTYDB.NOTIFICATION_TEAMHOMEPOINTS_COLUMN, notification.getTeamHomePoints());

            json.put(RUGBYPTYDB.NOTIFICATION_TEAMAWAY_COLUMN, notification.getTeamAwayName());
            json.put(RUGBYPTYDB.NOTIFICATION_TEAMAWAYFLAG_COLUMN, notification.getTeamAwayFlag());
            json.put(RUGBYPTYDB.NOTIFICATION_TEAMAWAYPOINTS_COLUMN, notification.getTeamAwayPoints());

            json.put(RUGBYPTYDB.NOTIFICATION_DATE_COLUMN, notification.getDate());
            json.put(RUGBYPTYDB.NOTIFICATION_TIME_COLUMN, notification.getTime());
            json.put(RUGBYPTYDB.NOTIFICATION_PLACE_COLUMN, notification.getPlace());
            json.put(RUGBYPTYDB.NOTIFICATION_PLAYED_COLUMN, notification.isWasPlayed());

            Debug.i(json.toString());
            return json;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
