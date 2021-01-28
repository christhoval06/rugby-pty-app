package app.christhoval.rugbypty.adapters.spinners;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.adapters.base.BaseArrayAdapter;
import app.christhoval.rugbypty.models.Tournament;
import app.christhoval.rugbypty.utilities.Debug;

/**
 * Created by christhoval
 * on 08/08/16.
 */
public class TournamentAdapter extends BaseArrayAdapter<Tournament> {

    public TournamentAdapter(Context context) {
        super(context, R.layout.view_item_spinner_tournament);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        Tournament tournament = getItem(position);
        return render(tournament, convertView, parent);
    }

    public void replaceData(List<Tournament> items) {
        setItems(items);
        notifyDataSetChanged();
    }

    private View render(Tournament tournament, View convertView, ViewGroup parent) {
        Debug.i("Tournament: " + tournament);
        View row = getLayoutInflater(parent).inflate(R.layout.view_item_spinner_tournament, parent, false);
        TextView text = (TextView) row.findViewById(R.id.tournament);
        Debug.i("tournament.name: " + tournament.getName());
        text.setText(tournament.getName());
        row.setTag(tournament);
        return row;
    }
}
