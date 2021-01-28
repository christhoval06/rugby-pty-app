package app.christhoval.rugbypty.utilities.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class DB {
    private static String TAG = DB.class.getSimpleName();
    private SQLiteDatabase db = null;

    public DB(Context context) {
        Context nContext = context;
        DBHelper dbHelper = DBHelper.getInstance(nContext);
        db = dbHelper.getWritableDatabase();
        if (db != null) {
        }
    }

    public SQLiteDatabase getDB() {
        return this.db;
    }

    public JSONObject sql2JSONOBJECT(String sql, String items) {
        return sql2JSONOBJECT(db.rawQuery(sql, null), items);
    }

    public JSONObject sql2JSONOBJECT(Cursor c, String items) {
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    JSONObject json = new JSONObject();
                    JSONArray data = new JSONArray();
                    json.put(items, data);
                    do {
                        JSONObject j = new JSONObject();
                        for (String item : c.getColumnNames())
                            j.put(c.getColumnName(c.getColumnIndex(item)), c.getString(c.getColumnIndex(item)));
                        data.put(j);
                    } while (c.moveToNext());

                    return json;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } finally {
                c.close();
            }
        }
        return null;
    }

    public JSONObject sql2JSONARRAY(String sql) {
        return sql2JSONARRAY(db.rawQuery(sql, null));
    }

    public JSONObject sql2JSONARRAY(Cursor c) {
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    JSONObject json = new JSONObject();
                    do {
                        for (String item : c.getColumnNames()) {
                            json.put(c.getColumnName(c.getColumnIndex(item)), c.getString(c.getColumnIndex(item)));
                        }
                    } while (c.moveToNext());
                    return json;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } finally {
                c.close();
            }
        }
        return null;
    }

    public boolean borrarTabla(String tabla) {
        return db.delete(tabla, null, null) > 0;
    }

    public boolean save(String tabla, JSONObject json) {
        return db.insert(tabla, null, JsonToContentValues(json)) > 0;
    }

    private ContentValues JsonToContentValues(JSONObject json) {
        ContentValues cv = new ContentValues();
        try {
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                cv.put(key, json.getString(key));
            }
            return cv;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean update(String tabla, JSONObject json, String where, String[] whereArgs) {
        return db.update(tabla, JsonToContentValues(json), where, whereArgs) >0; //new String[]{"1"}) > 0;
    }


    public boolean delete(String tabla, String where, String[] whereArgs) {
        return db.delete(tabla, where, whereArgs) >0;
    }

    public JSONObject getAll(String tabla, String key) {
        return sql2JSONOBJECT("SELECT * FROM " + tabla, key);
    }

    public JSONObject getAll(Cursor c, String key) {
        return sql2JSONOBJECT(c, key);
    }

    public boolean have(String tabla) {
        return have(db.rawQuery("SELECT * FROM " + tabla + " LIMIT 1;", null));
    }

    public boolean exist(String sql) {
        return have(db.rawQuery(sql, null));
    }

    public boolean have(Cursor c) {
        if (c != null) {
            try {
                if (c.moveToFirst()) return true;
            } finally {
                c.close();
            }
        }
        return false;
    }
}