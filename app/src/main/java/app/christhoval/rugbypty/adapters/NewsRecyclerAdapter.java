package app.christhoval.rugbypty.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.adapters.base.BaseRecyclerAdapter;
import app.christhoval.rugbypty.fragments.NewsFragment;
import app.christhoval.rugbypty.interfaces.onRecyclerViewClickListener;
import app.christhoval.rugbypty.models.Post;

public class NewsRecyclerAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder, Post> {
    private Context _context;

    protected NewsFragment.LayoutManagerType _currentLayoutManagerType;

    private onRecyclerViewClickListener _listener;

    public NewsRecyclerAdapter(onRecyclerViewClickListener clickListener) {
        _listener = clickListener;
    }

    public NewsRecyclerAdapter(onRecyclerViewClickListener clickListener, NewsFragment.LayoutManagerType _currentLayoutManagerType) {
        _listener = clickListener;
        this._currentLayoutManagerType = _currentLayoutManagerType;
    }

    public NewsRecyclerAdapter(onRecyclerViewClickListener clickListener, List<Post> postList, NewsFragment.LayoutManagerType _currentLayoutManagerType) {
        _listener = clickListener;
        this._currentLayoutManagerType = _currentLayoutManagerType;
        replaceData(postList);
    }

    public void replaceData(List<Post> items) {
        setItems(items);
        notifyDataSetChanged();
    }

    private View getViewHolder(ViewGroup parent) {
        int layout = _currentLayoutManagerType == NewsFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER ? R.layout.view_item_new_list : R.layout.view_item_new_grid;

        return getLayoutInflater(parent).inflate(layout, parent, false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this._context = parent.getContext();
        return new NewsViewHolder(getViewHolder(parent));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Post post = getItem(position);
        NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
        newsViewHolder.render(newsViewHolder, post, position);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView date;
        public ImageView image;
        ProgressBar progressBar;
        View root;

        NewsViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            image = (ImageView) itemView.findViewById(R.id.image);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }

        void render(final RecyclerView.ViewHolder holder, final Post post, final int position) {

            title.setText(post.getTitle());
            ViewCompat.setTransitionName(title, String.valueOf(post.getId()) + "_title");

            date.setText(post.getFormatedPublishedDate());
            Picasso.with(_context)
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
            ViewCompat.setTransitionName(image, String.valueOf(post.getId()) + "_image");

            itemView.setTag(post);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _listener.onRowClicked(holder, post, position);
                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _listener.onViewClicked(holder, view, post, position);
                }
            });
        }
    }
}
