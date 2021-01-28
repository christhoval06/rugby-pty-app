package app.christhoval.rugbypty.utilities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by christhoval
 * Date 06/06/16.
 */
public class SharedPreferences {
    private android.content.SharedPreferences sharedPref;
    private android.content.SharedPreferences.Editor editor;

    private AppCompatActivity activity;
    private Context context;

    public static SharedPreferences getInstance(Context context) {
        return new SharedPreferences(context);
    }

    public SharedPreferences(AppCompatActivity a) {
        activity = a;
        initPreferences();
    }

    public SharedPreferences(Context context) {
        this.context = context;
        initPreferences();
    }

    private void initPreferences() {
        if (activity != null)
            sharedPref = activity.getSharedPreferences(Constant.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        else
            sharedPref = context.getSharedPreferences(Constant.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        editor = sharedPref.edit();
        editor.apply();
    }

    public String get(String key, String defvalue) {
        return sharedPref.getString(key, defvalue);
    }

    public void set(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void set(String[] names, String[] values) {
        for (int i = 0; i < names.length; i++) {
            editor.putString(names[i], values[i]);
        }
        editor.commit();
    }

    public void clearPreference(String... names) {
        for (int i = 0; i < names.length; i++) {
            editor.putString(names[i], "");
        }
        editor.commit();
    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }

    public android.content.SharedPreferences getSharedPref() {
        return sharedPref;
    }

    public android.content.SharedPreferences.Editor getEditor() {
        return editor;
    }
}
