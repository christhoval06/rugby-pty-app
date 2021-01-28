package app.christhoval.rugbypty.models;

/**
 * Created by christhoval
 * on 11/18/16.
 */

public class OrderBy {
    private String id;
    private String name;

    public OrderBy(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
