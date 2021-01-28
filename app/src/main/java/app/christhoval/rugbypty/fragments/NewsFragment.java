package app.christhoval.rugbypty.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.activities.Main;
import app.christhoval.rugbypty.adapters.NewsRecyclerAdapter;
import app.christhoval.rugbypty.fragments.base.BaseFragment;
import app.christhoval.rugbypty.interfaces.RugbyPTYService;
import app.christhoval.rugbypty.interfaces.onRecyclerViewClickListener;
import app.christhoval.rugbypty.models.Post;
import app.christhoval.rugbypty.models.response.NewsList;
import app.christhoval.rugbypty.transitions.SampleTransition;
import app.christhoval.rugbypty.utilities.Constant;
import app.christhoval.rugbypty.utilities.Debug;
import app.christhoval.rugbypty.utilities.RugbyPTYHTTP;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends BaseFragment implements onRecyclerViewClickListener {

    public static final String DEEP_LINK = Constant.FRAGMENT_NEWS;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private ProgressBar _loading;

    public enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType _currentLayoutManagerType;

    protected RecyclerView _recyclerView;
    private NewsRecyclerAdapter _adapter;
    protected RecyclerView.LayoutManager _layoutManager;

    protected List<Post> _postsList;

    private AppBarLayout appBarLayout;
    private View newsFilters;

    public static NewsFragment newInstance(Bundle extras) {
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onAttach(Context context) {
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            _currentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newsFilters = inflater.inflate(R.layout.view_news_filter_bar, container, false);
        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.filter_bar_height));
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);

        appBarLayout.addView(newsFilters, params);

        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPosts();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (_recyclerView != null) {
            _recyclerView.setHasFixedSize(true);
            _layoutManager = new LinearLayoutManager(getContext());
            if (null == _currentLayoutManagerType) {
                _currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
            }
            setRecyclerViewLayoutManager(_currentLayoutManagerType);
            //_recyclerView.setTopBar(findViewById(R.id.news_filter_bar));

            AppCompatImageButton changeLayout = (AppCompatImageButton) newsFilters.findViewById(R.id.change_layout);
            changeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _currentLayoutManagerType = _currentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER ? LayoutManagerType.GRID_LAYOUT_MANAGER : LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                    setRecyclerViewLayoutManager(_currentLayoutManagerType);
                }
            });
        }

        _loading = (ProgressBar) findViewById(R.id.loading);
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;
        if (_recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) _recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                _layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                _currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
            default:
                _layoutManager = new LinearLayoutManager(getActivity());
                _currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        if (null != _postsList) {
            _adapter = new NewsRecyclerAdapter(this, _postsList, _currentLayoutManagerType);
        } else {
            _adapter = new NewsRecyclerAdapter(this, _currentLayoutManagerType);
        }
        //_adapter = new NewsRecyclerAdapter(this);
        _recyclerView.setAdapter(_adapter);
        _recyclerView.setLayoutManager(_layoutManager);
        _recyclerView.scrollToPosition(scrollPosition);
    }

    private void noData() {
        toggleView(R.id.no_data);
    }

    private void getPosts() {
        Map<String, String> params = new HashMap<>();
        params.put("filters", "{\"state\":\"published\",\"format\":\"standard\"}");
        params.put("limit", "30");
        params.put("sort", "-publishedDate");

        RugbyPTYService rugbyPTYService = RugbyPTYHTTP.getClient().create(RugbyPTYService.class);
        Call<NewsList> newsListCall = rugbyPTYService.listNews(params);
        newsListCall.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                _postsList = response.body().getNews();
                if (_postsList.size() > 0) {
                    fillNewsList(_postsList);
                } else {
                    noData();
                }
                _loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {
                Debug.i(t.toString());
                _loading.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onRowClicked(RecyclerView.ViewHolder holder, Object obj, int position) {

    }

    @Override
    public void onViewClicked(RecyclerView.ViewHolder holder, View v, Object obj, int position) {
        Post post = (Post) obj;
        Main app = (Main) getActivity();
        switch (v.getId()) {
            case R.id.image:
                Bundle bundle = new Bundle();
                bundle.putSerializable(NewsViewerFragment.EXTRA_POST, post);

                NewsViewerFragment newsViewer = NewsViewerFragment.newInstance(bundle);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    /*
                    Transition changeTransform = TransitionInflater.from(app).inflateTransition(R.transition.change_image_transform);
                    Transition explodeTransform = TransitionInflater.from(app).inflateTransition(android.R.transition.explode);

                    newsViewer.setSharedElementEnterTransition(changeTransform);
                    newsViewer.setEnterTransition(explodeTransform);
                    */

                    newsViewer.setSharedElementEnterTransition(new SampleTransition());
                    newsViewer.setEnterTransition(new Fade());
                    setExitTransition(new Fade());
                    newsViewer.setSharedElementReturnTransition(new SampleTransition());
                }

                app.getSupportFragmentManager()
                        .beginTransaction()
                        // Todo: animation in SharedElement only works to replace
                        .addSharedElement(((NewsRecyclerAdapter.NewsViewHolder) holder).image, "image")
                        .addSharedElement(((NewsRecyclerAdapter.NewsViewHolder) holder).title, "title")
                        //.add(R.id.content, newsViewer, "news_viewer")
                        //.add(R.id.content, newsViewer)
                        .replace(R.id.content, newsViewer)
                        .addToBackStack("news_viewer")
                        //.addToBackStack(null)
                        .commit();
                break;
        }
    }


    private void fillNewsList(List<Post> news) {
        if (null != news) {
            _adapter.replaceData(news);
        }
    }

    @Override
    public void onPause() {
        if (newsFilters.getParent() != null) {
            appBarLayout.removeView(newsFilters);
        }
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, _currentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDetach() {
        if (newsFilters.getParent() != null) {
            appBarLayout.removeView(newsFilters);
        }
        super.onDetach();
    }
}



