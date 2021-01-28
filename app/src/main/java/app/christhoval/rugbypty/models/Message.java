package app.christhoval.rugbypty.models;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.christhoval.rugbypty.utilities.Debug;
import app.christhoval.rugbypty.utilities.Utils;
import app.christhoval.rugbypty.utilities.db.RUGBYPTYDB;

/**
 * Created by christhoval on 08/09/16.
 */

public class Message {
    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;

    public static String MESSAGES = "messages";
    public static final String TYPE_MESSAGE_TEXT = "text";
    public static final String TYPE_MESSAGE_IMAGE = "image";

    public String id;
    private int type;
    private String date;
    private String message;
    private String message_type;
    private String username;
    private Bitmap image;

    private Message() {
    }

    public String getId() {
        return id;
    }

    public String getTypeMessage() {
        return message_type;
    }

    public int getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getDate() {
        return Utils.formatDate(date, "dd 'de' MMMM 'de' yyyy");
    }

    public String getTimeStamp() {
        return Utils.formatDate(date, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public String getTime() {
        return Utils.formatDate(date, "hh:mm a");
    }


    public static class Builder {
        private final int mType;
        private Bitmap mImage;
        private String mMessage;
        private String mUsername;
        private String mMessageType;

        public Builder(int type) {
            mType = type;
        }

        public Builder type(String type) {
            mMessageType = type;
            return this;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder image(Bitmap image) {
            mImage = image;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.type = mType;
            message.image = mImage;
            message.message = mMessage;
            message.username = mUsername;
            message.message_type = mMessageType;
            return message;
        }
    }


    public static Message fromJSON(JSONObject json) {
        Message message = new Message();
        try {
            message.id = json.getString("_id");
            message.type = json.getString("type").equals(TYPE_MESSAGE_TEXT) ? 0 : 0;
            message.username = json.getString("username");
            message.message = json.getString("message");
            message.date = json.getString("createdAt");

            Debug.i(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return message;
    }

    public static ArrayList<Message> fromJson(JSONArray jsonArray) {
        JSONObject json;
        ArrayList<Message> messages = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Message message = Message.fromJSON(json);
            if (message != null) {
                messages.add(message);
            }
        }

        return messages;
    }

    public static JSONObject toJSon(Message message) {
        try {
            JSONObject json = new JSONObject();
            json.put(RUGBYPTYDB.MESSAGE_ID_COLUMN, message.getId());
            json.put(RUGBYPTYDB.MESSAGE_TYPE_COLUMN, message.getType());
            json.put(RUGBYPTYDB.MESSAGE_USERNAME_COLUMN, message.getUsername());
            json.put(RUGBYPTYDB.MESSAGE_MESSAGE_COLUMN, message.getMessage());
            json.put(RUGBYPTYDB.MESSAGE_DATE_COLUMN, message.getTimeStamp());

            Debug.i(json.toString());
            return json;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
