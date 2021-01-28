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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.adapters.PositionRecyclerAdapter;
import app.christhoval.rugbypty.fragments.base.BaseFragment;
import app.christhoval.rugbypty.interfaces.RugbyPTYService;
import app.christhoval.rugbypty.models.OrderBy;
import app.christhoval.rugbypty.models.TeamInTournament;
import app.christhoval.rugbypty.models.Tournament;
import app.christhoval.rugbypty.models.response.PositionsTable;
import app.christhoval.rugbypty.models.response.TournamentList;
import app.christhoval.rugbypty.utilities.Constant;
import app.christhoval.rugbypty.utilities.Debug;
import app.christhoval.rugbypty.utilities.RugbyPTYHTTP;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PositionFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    public static final String DEEP_LINK = Constant.FRAGMENT_POSITIONS;
    private String _tournamentId;
    private String _orderBy;
    private String _orderByDirection;

    private PositionRecyclerAdapter _adapter;

    private List<Tournament> _tournamentList;
    private List<TeamInTournament> _teamInTournamentsList = new ArrayList<>();

    private AppBarLayout appBarLayout;
    private View positionsFilters;

    private AppCompatSpinner _orderBySpinner;
    private AppCompatSpinner _orderByDirectionSpinner;
    private RugbyPTYService rugbyPTYService;

    public static PositionFragment newInstance(String tournamentId) {
        PositionFragment fragment = new PositionFragment();
        Bundle args = new Bundle();
        args.putString(Constant.TOURNAMENT_ID, tournamentId);
        fragment.setArguments(args);
        Debug.i("newInstance PositionFragment");
        return fragment;
    }

    public static PositionFragment newInstance(Bundle extras) {
        PositionFragment fragment = new PositionFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static PositionFragment newInstance() {
        return new PositionFragment();
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
            _tournamentId = getArguments().getString(Constant.TOURNAMENT_ID);
        }
        rugbyPTYService = RugbyPTYHTTP.getClient().create(RugbyPTYService.class);

        setRetainInstance(true);

        Debug.i("onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Debug.i("onCreateView");

        positionsFilters = inflater.inflate(R.layout.view_position_filter_bar, container, false);
        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.filter_bar_height));
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);

        appBarLayout.addView(positionsFilters, params);

        return inflater.inflate(R.layout.fragment_position, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            _tournamentId = savedInstanceState.getString("_tournamentId");
            _orderBy = savedInstanceState.getString("_orderBy");
            _orderByDirection = savedInstanceState.getString("_orderByDirection");
        }

        Debug.i("onActivityCreated");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            _adapter = new PositionRecyclerAdapter();
            recyclerView.setAdapter(_adapter);
            //recyclerView.addItemDecoration(new ItemDecoration((PositionAdapter) recyclerView.getAdapter(), getResources()));
            recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(_adapter));
        }

        setVisibility(R.id.recycler_view, View.GONE);
        setVisibility(R.id.no_data, View.GONE);
        setVisibility(R.id.loading, View.VISIBLE);

        getTournamentsFromAPI();

        Debug.i("onViewCreated");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            _tournamentId = savedInstanceState.getString("_tournamentId");
            _orderBy = savedInstanceState.getString("_orderBy");
            _orderByDirection = savedInstanceState.getString("_orderByDirection");
        }

        Debug.i("onViewStateRestored");
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

    private void populateOrderBySpinner() {
        List<OrderBy> orderByList = new ArrayList<>();
        orderByList.add(new OrderBy("tp", getString(R.string.orderby_points)));
        orderByList.add(new OrderBy("tf", getString(R.string.orderby_tries)));
        orderByList.add(new OrderBy("tc", getString(R.string.orderby_tries_against)));
        orderByList.add(new OrderBy("td", getString(R.string.orderby_tries_diference)));
        orderByList.add(new OrderBy("cr", getString(R.string.orderby_conversion)));
        orderByList.add(new OrderBy("pr", getString(R.string.orderby_penals)));
        orderByList.add(new OrderBy("gp", getString(R.string.orderby_game_played)));
        orderByList.add(new OrderBy("lf", getString(R.string.orderby_forfeit)));
        orderByList.add(new OrderBy("gw", getString(R.string.orderby_game_won)));
        orderByList.add(new OrderBy("gl", getString(R.string.orderby_game_lost)));
        orderByList.add(new OrderBy("gd", getString(R.string.orderby_game_draw)));
        orderByList.add(new OrderBy("name", getString(R.string.orderby_name)));

        _orderBySpinner = (AppCompatSpinner) appBarLayout.findViewById(R.id.sp_orderby);
        ArrayAdapter<OrderBy> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, orderByList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _orderBySpinner.setAdapter(adapter);
        _orderBySpinner.setOnItemSelectedListener(this);

        _orderBySpinner.setTag(R.string.spinner_adapter_first_item, orderByList.get(0));

        if (_orderBy == null) {
            _orderBy = _orderByDirection + orderByList.get(0).getId();
        }
    }

    private void populateOrderByDirectionSpinner() {
        List<OrderBy> orderByList = new ArrayList<>();
        orderByList.add(new OrderBy("", getString(R.string.orderby_asc)));
        orderByList.add(new OrderBy("-", getString(R.string.orderby_desc)));

        _orderByDirectionSpinner = (AppCompatSpinner) appBarLayout.findViewById(R.id.sp_orderby_direction);
        ArrayAdapter<OrderBy> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, orderByList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _orderByDirectionSpinner.setAdapter(adapter);
        _orderByDirectionSpinner.setOnItemSelectedListener(this);
        _orderByDirectionSpinner.setTag(R.string.spinner_adapter_first_item, orderByList.get(0));


        if (_orderByDirection == null) {
            _orderByDirection = orderByList.get(0).getId();
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

            getPositionsFromAPI(_tournamentId, _orderBy);
        }
    }

    private void populateTeams() {
        loaded(true);
        noData(_teamInTournamentsList.isEmpty());
        if (!_teamInTournamentsList.isEmpty()) {
            setVisibility(R.id.recycler_view, View.VISIBLE);
            _adapter.reOrderDataBy(_teamInTournamentsList, _orderBy, _orderByDirection);
        }
    }

    private void loaded(boolean loaded) {
        setVisibility(R.id.loading, loaded ? View.GONE : View.VISIBLE);
    }

    private void noData(boolean visible) {
        setVisibility(R.id.no_data, visible ? View.VISIBLE : View.GONE);
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
                populateOrderByDirectionSpinner();
                populateOrderBySpinner();
            }

            @Override
            public void onFailure(Call<TournamentList> call, Throwable t) {
                Debug.i(t.toString());
            }
        });
    }

    private void getPositionsFromAPI(final String tournamentId, final String orderBy) {
        setVisibility(R.id.loading, View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        if (tournamentId != null) {
            params.put("filters", String.format("{\"_id\":\"%1$s\"}", tournamentId));
            params.put("sort", orderBy);
        }

        Call<PositionsTable> newsListCall = rugbyPTYService.getPositions(params);
        newsListCall.enqueue(new Callback<PositionsTable>() {
            @Override
            public void onResponse(Call<PositionsTable> call, Response<PositionsTable> response) {
                _teamInTournamentsList = response.body().getTeams();
                populateTeams();
            }

            @Override
            public void onFailure(Call<PositionsTable> call, Throwable t) {
                Debug.i(t.toString());
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Debug.i("onSaveInstanceState");
        outState.putString("_tournamentId", _tournamentId);
        outState.putString("_orderBy", _orderBy);
        outState.putString("_orderByDirection", _orderByDirection);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        appBarLayout.removeView(positionsFilters);
        super.onDetach();
    }

    /**
     * setOnItemSelectedListener implementation
     */

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        switch (adapterView.getId()) {
            case R.id.sp_tournament:
                _tournamentId = ((Tournament) adapterView.getItemAtPosition(position)).getId();
                getPositionsFromAPI(_tournamentId, _orderBy);
                _orderBySpinner.setSelection(0);
                _orderByDirectionSpinner.setSelection(0);
                _orderBy = _orderBySpinner.getTag(R.string.spinner_adapter_first_item).toString();
                _orderByDirection = _orderByDirectionSpinner.getTag(R.string.spinner_adapter_first_item).toString();
                break;

            case R.id.sp_orderby:
                _orderBy = ((OrderBy) adapterView.getItemAtPosition(position)).getId();
                populateTeams();
                break;

            case R.id.sp_orderby_direction:
                _orderByDirection = ((OrderBy) adapterView.getItemAtPosition(position)).getId();
                populateTeams();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
