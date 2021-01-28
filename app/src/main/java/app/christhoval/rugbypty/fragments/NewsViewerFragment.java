package app.christhoval.rugbypty.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.fragments.base.BaseFragment;
import app.christhoval.rugbypty.models.Post;

/**
 * Created by: christhoval
 * on: 08/22/16.
 */
public class NewsViewerFragment extends BaseFragment {

    public static String EXTRA_POST = "post";
    public static String EXTRA_POSTID = "post_id";

    private ImageView image;
    private TextView title, author, date;
    private WebView web;
    private ProgressBar progressBar;

    private Post post;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private View newsImage;

    public static NewsViewerFragment newInstance(Bundle extras) {
        NewsViewerFragment fragment = new NewsViewerFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static NewsViewerFragment newInstance() {
        return new NewsViewerFragment();
    }

    @Override
    public void onAttach(Context context) {
        collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsingtoolbar);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(EXTRA_POST)) {
                post = (Post) getArguments().getSerializable(EXTRA_POST);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        newsImage = inflater.inflate(R.layout.view_news_image, container, false);
        CollapsingToolbarLayout.LayoutParams params = new CollapsingToolbarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.news_image_height));
        params.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);

        if (null != getActivity().getActionBar()) {
            getActivity().getActionBar().setHomeButtonEnabled(true);
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbarLayout.addView(newsImage, params);
        return inflater.inflate(R.layout.fragment_news_viewer, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setupToolBarNavigationIcon(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image = (ImageView) collapsingToolbarLayout.findViewById(R.id.news_image);
        title = (TextView) view.findViewById(R.id.title);
        author = (TextView) view.findViewById(R.id.autor);
        date = (TextView) view.findViewById(R.id.date);
        web = (WebView) view.findViewById(R.id.webView);
        progressBar = (ProgressBar) view.findViewById(R.id.loading);
        renderPost(post);
    }


    private void renderPost(Post post) {
        if (post != null) {

            updateTitle(post.getTitle());


            title.setText(post.getTitle());
            author.setText(post.getAuthor().getName().getFull());
            date.setText(post.getFormatedPublishedDate());
            Picasso.with(getActivity())
                    .load(post.getImage().getThumb("600", "350"))
                    .placeholder(R.drawable.image_placeholder)
                    //.error(R.drawable.placeholder)
                    .into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            web.loadData(post.getContent().getExtended(), "text/html", "UTF-8");
            web.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onDetach() {
        collapsingToolbarLayout.removeView(newsImage);
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}