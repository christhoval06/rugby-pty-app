package app.christhoval.rugbypty.adapters.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by christhoval
 * on 11/18/16.
 */

public abstract class BaseArrayAdapter<M> extends ArrayAdapter<M> {
    private List<M> items = Collections.EMPTY_LIST;

    public BaseArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void setItems(List<M> items) {
        validateItems(items);
        this.items = items;
    }

    private void validateItems(List<M> items) {
        if (items == null) {
            throw new IllegalArgumentException("You can't use a null List<Item> instance.");
        }
    }

    public LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }

    public M getItem(int position) {
        return items.get(position);
    }

    public List<M> getItems() {
        return items;
    }
}
