package app.christhoval.rugbypty.utilities.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import app.christhoval.rugbypty.utilities.Constant;

public class DBHelper extends SQLiteOpenHelper {

    public static String TAG = DBHelper.class.getSimpleName();
    private static DBHelper sInstance = null;
    public static final int VERSION_DB = Constant.DB_VERSION;
    private static String DBNAME = Constant.DB_NAME;

    public static DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext(), DBNAME, null, VERSION_DB);
        }
        return sInstance;
    }

    private DBHelper(Context _contexto, String db, CursorFactory factory, int version) {
        super(_contexto, db, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTablas(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            recrearTablas(db);
        } else {
            recrearTablas(db);
        }
    }

    public void recrearTablas(SQLiteDatabase db) {
        dropTablas(db);
        createTablas(db);
    }

    public void create(SQLiteDatabase db) {
        createTablas(db);
    }

    private void createTablas(SQLiteDatabase db) {
        db.execSQL(RUGBYPTYDB.getSQLForTableFavoritos());
        db.execSQL(RUGBYPTYDB.getSQLForTableChat());
    }

    private void dropTablas(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + RUGBYPTYDB.TABLE_NOTIFICATION);
        db.execSQL("DROP TABLE IF EXISTS " + RUGBYPTYDB.TABLE_MESSAGE);
    }
}