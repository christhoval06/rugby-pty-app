package app.christhoval.rugbypty.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by christhoval
 * on 07/26/16.
 */
public class Category implements Serializable {
    public String _id;
    public String name;

    public static Category fromJSON(JSONObject json) {
        Category category = new Category();
        try {
            category._id = json.getString("_id");
            category.name = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return category;
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public static ArrayList<Category> fromJSON(JSONArray jsonArray) {
        JSONObject json;
        ArrayList<Category> categories = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Category category = Category.fromJSON(json);
            if (category != null) {
                categories.add(category);
            }
        }

        return categories;
    }
}
