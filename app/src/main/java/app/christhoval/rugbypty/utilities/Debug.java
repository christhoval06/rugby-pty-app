package app.christhoval.rugbypty.utilities;

import android.util.Log;

/**
 * Created by christhoval
 * Date 06/06/16.
 */
public class Debug {
    public static void i(String Constanttant, String message) {
        if (Constant.ENABLE_DEBUG) {
            Log.i(Constanttant, message);
        }
    }

    public static void i(String message) {
        Debug.i(Constant.TAG, message);
    }

    public static void e(String tag, String message) {
        if (Constant.ENABLE_DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(String message) {
        if (Constant.ENABLE_DEBUG) {
            Debug.e(Constant.TAG, message);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if (Constant.ENABLE_DEBUG) {
            Log.e(tag, message);

            e.printStackTrace();
        }
    }
}
