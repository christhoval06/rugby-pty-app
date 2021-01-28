package app.christhoval.rugbypty.fragments.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidquery.AQuery;

import java.util.HashMap;
import java.util.Map;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.interfaces.RugbyPTYService;
import app.christhoval.rugbypty.models.response.TournamentList;
import app.christhoval.rugbypty.utilities.Debug;
import app.christhoval.rugbypty.utilities.Request;
import app.christhoval.rugbypty.utilities.RugbyPTYHTTP;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by christhoval on 08/22/16.
 */
public abstract class TabsParentFragment extends Fragment {
    private Request request;
    public AppBarLayout appBarLayout;
    public TabLayout tabLayout;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        request = new Request(new AQuery(getActivity()));
        Debug.i("TabsParentFragment -> onActivityCreated");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        tabLayout = (TabLayout) inflater.inflate(R.layout.view_tablayout, null);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        appBarLayout.addView(tabLayout, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Debug.i("TabsParentFragment -> onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void getTournaments(Callback<TournamentList> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("filters", "{\"inProgress\":\"true\"}");
        RugbyPTYService rugbyPTYService = RugbyPTYHTTP.getClient().create(RugbyPTYService.class);
        Call<TournamentList> tournamentListCall = rugbyPTYService.listTournament(params);
        tournamentListCall.enqueue(callback);
    }

    public void allotEachTabWithEqualWidth(TabLayout tabLayout) {

        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tab = slidingTabStrip.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab.getLayoutParams();
            layoutParams.weight = 1;
            tab.setLayoutParams(layoutParams);
        }

    }

    @Override
    public void onDetach() {
        appBarLayout.removeView(tabLayout);
        super.onDetach();
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }
}
