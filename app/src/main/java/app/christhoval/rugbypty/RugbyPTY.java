package app.christhoval.rugbypty;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

import java.net.URISyntaxException;

import app.christhoval.rugbypty.utilities.Constant;
import app.christhoval.rugbypty.utilities.SharedPreferences;
import app.christhoval.rugbypty.utilities.db.RUGBYPTYDB;
import io.socket.client.IO;
import io.socket.client.Socket;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class RugbyPTY extends Application {

    private RUGBYPTYDB db;
    private SharedPreferences preferences;
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(Constant.SOCKET_SERVER);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build());

        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);

        db = new RUGBYPTYDB(getApplicationContext());

        //SystemClock.sleep(TimeUnit.SECONDS.toMillis(2));

        ActiveAndroid.initialize(this);
        preferences = new SharedPreferences(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public Socket getSocket() {
        return mSocket;
    }

    public RUGBYPTYDB getDb() {
        return db;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }
}
