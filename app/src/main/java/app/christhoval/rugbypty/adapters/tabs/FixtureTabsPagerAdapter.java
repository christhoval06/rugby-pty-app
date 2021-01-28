package app.christhoval.rugbypty.adapters.tabs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import app.christhoval.rugbypty.fragments.FixtureFragment;
import app.christhoval.rugbypty.fragments.PositionFragment;
import app.christhoval.rugbypty.models.Tournament;
import app.christhoval.rugbypty.utilities.Debug;

/**
 * Created by christhoval
 * on 08/18/16.
 */
public class FixtureTabsPagerAdapter extends FragmentPagerAdapter {
    private List<Tournament> tournaments = new ArrayList<>();
    private Context context;

    public FixtureTabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return tournaments.size();
    }

    @Override
    public Fragment getItem(int position) {

        Debug.i("position: " + position);
        return FixtureFragment.newInstance(tournaments.get(position).getId());
    }

    @Override
    public CharSequence getPageTitle(int position) {

        Debug.i("getPageTitle: " + position);
        return tournaments.get(position).getName();
    }


    public void replaceData(List<Tournament> items) {
        setList(items);
        notifyDataSetChanged();
    }

    public void setList(List<Tournament> list) {
        this.tournaments = list;
    }
}
