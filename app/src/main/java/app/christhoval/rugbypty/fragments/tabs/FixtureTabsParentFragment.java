package app.christhoval.rugbypty.fragments.tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.adapters.tabs.FixtureTabsPagerAdapter;
import app.christhoval.rugbypty.fragments.base.TabsParentFragment;
import app.christhoval.rugbypty.models.Tournament;
import app.christhoval.rugbypty.models.response.TournamentList;
import app.christhoval.rugbypty.utilities.Debug;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FixtureTabsParentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FixtureTabsParentFragment extends TabsParentFragment {
    private ViewPager viewPager;
    private FixtureTabsPagerAdapter adapter;
    private ProgressBar loading;

    private List<Tournament> tournaments;

    public FixtureTabsParentFragment() {
    }


    public static FixtureTabsParentFragment newInstance() {
        return new FixtureTabsParentFragment();
    }

    public static FixtureTabsParentFragment newInstance(Bundle args) {
        FixtureTabsParentFragment fragment = new FixtureTabsParentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Debug.i("onActivityCreated");
        getTournaments(new Callback<TournamentList>() {
            @Override
            public void onResponse(Call<TournamentList> call, Response<TournamentList> response) {
                tournaments = response.body().getTournaments();
                if (tournaments.size() > 0) {
                    adapter.replaceData(tournaments);
                    allotEachTabWithEqualWidth(tabLayout);
                    viewPager.setCurrentItem(0);
                }
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TournamentList> call, Throwable t) {
                Debug.i(t.toString());
                loading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_poisition_parent, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loading = (ProgressBar) view.findViewById(R.id.loading);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new FixtureTabsPagerAdapter(getChildFragmentManager(), view.getContext());

        viewPager.setAdapter(adapter);
        getTabLayout().setupWithViewPager(viewPager);
    }
}
