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
import app.christhoval.rugbypty.adapters.tabs.PositionTabsPagerAdapter;
import app.christhoval.rugbypty.fragments.base.TabsParentFragment;
import app.christhoval.rugbypty.models.Tournament;
import app.christhoval.rugbypty.models.response.TournamentList;
import app.christhoval.rugbypty.utilities.Debug;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PoisitionTabsParentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PoisitionTabsParentFragment extends TabsParentFragment {

    private ViewPager viewPager;
    private ProgressBar loading;

    private PositionTabsPagerAdapter adapter;
    private List<Tournament> tournaments;


    public PoisitionTabsParentFragment() {
    }


    public static PoisitionTabsParentFragment newInstance() {
        return new PoisitionTabsParentFragment();
    }

    public static PoisitionTabsParentFragment newInstance(Bundle args) {
        PoisitionTabsParentFragment fragment = new PoisitionTabsParentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

        adapter = new PositionTabsPagerAdapter(getChildFragmentManager(), view.getContext());

        viewPager.setAdapter(adapter);
        getTabLayout().setupWithViewPager(viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        Debug.i("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Debug.i("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Debug.i("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Debug.i("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Debug.i("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Debug.i("onDestroy");
    }
}
