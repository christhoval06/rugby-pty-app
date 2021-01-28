package app.christhoval.rugbypty.interfaces;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by christhoval on 08/22/16.
 */
public interface onRecyclerViewClickListener {
    void onRowClicked(RecyclerView.ViewHolder holder, Object obj, int position);

    void onViewClicked(RecyclerView.ViewHolder holder, View v, Object obj, int position);
}
