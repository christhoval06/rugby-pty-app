package app.christhoval.rugbypty.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.adapters.DividerDecoration;
import app.christhoval.rugbypty.adapters.FixtureHeadersRecyclerAdapter;
import app.christhoval.rugbypty.interfaces.RugbyPTYService;
import app.christhoval.rugbypty.models.Match;
import app.christhoval.rugbypty.models.MatchDay;
import app.christhoval.rugbypty.models.OrderBy;
import app.christhoval.rugbypty.models.Team;
import app.christhoval.rugbypty.models.Tournament;
import app.christhoval.rugbypty.models.response.FixturesResults;
import app.christhoval.rugbypty.models.response.TournamentList;
import app.christhoval.rugbypty.utilities.Debug;
import app.christhoval.rugbypty.utilities.RugbyPTYAPIHelpers;
import app.christhoval.rugbypty.utilities.RugbyPTYHTTP;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FixtureFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String DEEP_LINK = "fixtures";
    private static final String TOURNAMENT_ID = "tournament";

    private String _tournamentId;
    private String _team;
    private String _direction;

    private List<Tournament> _tournamentList;
    private List<MatchDay> _matchDays;

    private FixtureHeadersRecyclerAdapter adapter;
    private View emptyData;
    private ProgressBar loading;

    private AppBarLayout appBarLayout;
    private View fixtureFilters;

    private AppCompatSpinner _teamSpinner;
    private AppCompatSpinner _directionSpinner;
    RugbyPTYService rugbyPTYService;

    public static FixtureFragment newInstance(String tournamentid) {
        FixtureFragment fragment = new FixtureFragment();
        Bundle args = new Bundle();
        args.putString(TOURNAMENT_ID, tournamentid);
        fragment.setArguments(args);
        Debug.i("newInstance PositionFragment");
        return fragment;
    }

    public static FixtureFragment newInstance(Bundle extras) {
        FixtureFragment fragment = new FixtureFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static FixtureFragment newInstance() {
        return new FixtureFragment();
    }

    @Override
    public void onAttach(Context context) {
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _tournamentId = getArguments().getString(TOURNAMENT_ID);
        }
        Debug.i("_tournamentId: " + _tournamentId);

        rugbyPTYService = RugbyPTYHTTP.getClient().create(RugbyPTYService.class);
        //setHasOptionsMenu(true);
    }

    /**
     * MENU
     **/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.socket_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fixtureFilters = inflater.inflate(R.layout.view_fixture_filter_bar, container, false);
        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.filter_bar_height));
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);

        appBarLayout.addView(fixtureFilters, params);

        return inflater.inflate(R.layout.fragment_fixture, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMatchsFromAPI(_tournamentId);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        if (recyclerView != null) {
            adapter = new FixtureHeadersRecyclerAdapter();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            final StickyRecyclerHeadersDecoration headersDecoration = new StickyRecyclerHeadersDecoration(adapter);
            recyclerView.addItemDecoration(headersDecoration);
            recyclerView.addItemDecoration(new DividerDecoration(getActivity()));
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    headersDecoration.invalidateHeaders();
                }
            });
        }
        loading = (ProgressBar) view.findViewById(R.id.loading);
        emptyData = view.findViewById(R.id.no_data);

        getTournamentsFromAPI();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void populateTeamsSpinner(List<MatchDay> matchDays) {

        List<Team> teamList = new ArrayList<>();
        Team allTeam = new Team();
        allTeam.setId("all");
        allTeam.setShortName(getString(R.string.all));
        teamList.add(allTeam);

        for (Match match : RugbyPTYAPIHelpers.convertMatchDays2Matches(matchDays, _direction)) {
            if (!teamList.contains(match.getTeamA().getTeamRef())) {
                teamList.add(match.getTeamA().getTeamRef());
            }

            if (!teamList.contains(match.getTeamB().getTeamRef())) {
                teamList.add(match.getTeamB().getTeamRef());
            }
        }

        Collections.sort(teamList, new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                return o1.getShortName().compareTo(o2.getShortName());
            }
        });

        _teamSpinner = (AppCompatSpinner) appBarLayout.findViewById(R.id.sp_teams);
        ArrayAdapter<Team> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, teamList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _teamSpinner.setAdapter(adapter);
        _teamSpinner.setOnItemSelectedListener(this);

        _teamSpinner.setTag(R.string.spinner_adapter_first_item, teamList.get(0));

        if (_team == null) {
            _team = teamList.get(0).getId();
        }
    }

    private void populateDirectionSpinner() {
        List<OrderBy> orderByList = new ArrayList<>();
        orderByList.add(new OrderBy("+", getString(R.string.orderby_asc)));
        orderByList.add(new OrderBy("-", getString(R.string.orderby_desc)));

        _directionSpinner = (AppCompatSpinner) appBarLayout.findViewById(R.id.sp_direction);
        ArrayAdapter<OrderBy> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, orderByList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _directionSpinner.setAdapter(adapter);
        _directionSpinner.setOnItemSelectedListener(this);
        _directionSpinner.setTag(R.string.spinner_adapter_first_item, orderByList.get(0));


        if (_direction == null) {
            _direction = orderByList.get(0).getId();
        }

    }

    private void populateTournaments() {

        if (_tournamentList.size() > 0) {
            AppCompatSpinner spTournament = (AppCompatSpinner) appBarLayout.findViewById(R.id.sp_tournament);
            ArrayAdapter<Tournament> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, _tournamentList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spTournament.setAdapter(adapter);

            spTournament.setOnItemSelectedListener(this);

            if (_tournamentId == null) {
                _tournamentId = _tournamentList.get(0).getId();
            }

            getMatchsFromAPI(_tournamentId);
        }
    }

    private void noData() {
        emptyData.setVisibility(View.VISIBLE);
    }

    public void getTournamentsFromAPI() {
        Map<String, String> params = new HashMap<>();
        params.put("filters", "{\"inProgress\":\"true\"}");

        Call<TournamentList> newsListCall = rugbyPTYService.listTournament(params);
        newsListCall.enqueue(new Callback<TournamentList>() {
            @Override
            public void onResponse(Call<TournamentList> call, Response<TournamentList> response) {
                _tournamentList = response.body().getTournaments();
                populateTournaments();
            }

            @Override
            public void onFailure(Call<TournamentList> call, Throwable t) {
                Debug.i(t.toString());
            }
        });
    }

    public void getMatchsFromAPI(String tournament) {
        Map<String, String> params = new HashMap<>();

        if (tournament != null) {
            params.put("filters", String.format("{\"_id\":\"%1$s\"}", tournament));
        }

        Call<FixturesResults> fixturesResultsCall = rugbyPTYService.getMatchs(params);
        fixturesResultsCall.enqueue(new Callback<FixturesResults>() {
            @Override
            public void onResponse(Call<FixturesResults> call, Response<FixturesResults> response) {
                _matchDays = response.body().getMatchDays();
                if (_matchDays.size() > 0) {
                    populateDirectionSpinner();
                    populateTeamsSpinner(_matchDays);
                    _teamSpinner.setSelection(0);
                    _directionSpinner.setSelection(0);
                    _team = _teamSpinner.getTag(R.string.spinner_adapter_first_item).toString();
                    _direction = _directionSpinner.getTag(R.string.spinner_adapter_first_item).toString();
                    adapter.replaceData(_matchDays, _direction);
                } else {
                    noData();
                }
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FixturesResults> call, Throwable t) {
                Debug.i(t.toString());
                loading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDetach() {
        appBarLayout.removeView(fixtureFilters);
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * setOnItemSelectedListener implementation
     */

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        switch (adapterView.getId()) {
            case R.id.sp_tournament:
                _tournamentId = ((Tournament) adapterView.getItemAtPosition(position)).getId();
                getMatchsFromAPI(_tournamentId);
                break;

            case R.id.sp_teams:
                _team = ((Team) adapterView.getItemAtPosition(position)).getId();
                adapter.filter(_matchDays, _team, _direction);
                break;

            case R.id.sp_direction:
                _direction = ((OrderBy) adapterView.getItemAtPosition(position)).getId();
                adapter.replaceData(_matchDays, _direction);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

/*

http://robusttechhouse.com/tutorial-how-to-add-header-to-recyclerview-in-android/

 */
