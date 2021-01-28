package app.christhoval.rugbypty.utilities;

/**
 * Created by christhoval
 * Date 06/03/16.
 */
public class Constant {

    public static final String TAG = "Rugby+PTY";
    public static final String ACTUAL_DRAWER_TAG = "actual_drawer_tag";
    public static final String CHAT_USERNAME = "chat_username";
    public static final String SOCKET_SERVER = "https://rugbypty.herokuapp.com:8000";
    public static final String APP_LINK = "app_link";

    public static String SHARED_PREFERENCES = "rugbypty_prefs";
    public static String DB_NAME = "rugbyptydb";
    public static int DB_VERSION = 1;

    public static String FCM_NOTIFY = "fcm_notify";
    public static String FCM_TOKEN = "fcm_token";

    public static final boolean ENABLE_DEBUG = true;

    public static final String API_BASE_URL = "https://rugbypty.herokuapp.com/api";

    public static String cloudinay_url = "http://res.cloudinary.com/rugbypty/image/upload/c_thumb,f_auto,g_center,h_%1$s,w_%2$s/v%3$s/%4$s.%5$s";


    // Storage Permissions variables
    public static final int REQUEST_CODE_PERMISSIONS = 0x11;
    public static final int REQUEST_CODE_PERMISSIONS_CHAT = 0x12;


    /**
     * Fragments Names
     **/
    public static final String FRAGMENT_NEWS = "news";
    public static final String FRAGMENT_FIXTURES = "fixtures";
    public static final String FRAGMENT_POSITIONS = "positions";
    public static final String FRAGMENT_ABOUTUS = "aboutus";

    /**
     * VARS
     **/
    public static final String TOURNAMENT_ID = "tournament";
}
