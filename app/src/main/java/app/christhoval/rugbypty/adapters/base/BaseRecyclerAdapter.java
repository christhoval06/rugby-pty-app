package app.christhoval.rugbypty.adapters.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;


public abstract class BaseRecyclerAdapter<VH extends RecyclerView.ViewHolder, M> extends RecyclerView.Adapter<VH> {
    private List<M> items = Collections.EMPTY_LIST;

    public void setItems(List<M> items) {
        validateItems(items);
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public M getItem(int position) {
        return items.get(position);
    }

    public boolean hasItems() {
        return items.size() > 0;
    }

    private void validateItems(List<M> items) {
        if (items == null) {
            throw new IllegalArgumentException("You can't use a null List<Item> instance.");
        }
    }

    public LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }
}
