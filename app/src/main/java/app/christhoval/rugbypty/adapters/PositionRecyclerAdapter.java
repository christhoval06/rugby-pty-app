package app.christhoval.rugbypty.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.adapters.base.BaseRecyclerAdapter;
import app.christhoval.rugbypty.models.TeamInTournament;
import app.christhoval.rugbypty.utilities.Utils;

public class PositionRecyclerAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder, TeamInTournament> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private Context _context;
    private int _collapseColor;
    private int _expandColor;

    private SortedList<TeamInTournament> _items;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = getLayoutInflater(parent).inflate(R.layout.view_item_position, parent, false);
        _context = parent.getContext();
        _collapseColor = Utils.getColor(_context, android.R.color.transparent);
        _expandColor = Utils.getColor(_context, R.color.positions_row_bg);
        return new PositionViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        TeamInTournament teamInTournament = getItem(position);
        PositionViewHolder viewHolder = (PositionViewHolder) holder;
        viewHolder.render(holder, teamInTournament);
    }

    private class ExpandView implements View.OnClickListener {
        PositionViewHolder holder;
        TeamInTournament team;

        ExpandView(PositionViewHolder holder, TeamInTournament team) {
            this.holder = holder;
            this.team = team;
        }

        @Override
        public void onClick(View v) {
            int rotationAngle = (int) v.getTag(R.string.angle);
            TeamInTournament team = (TeamInTournament) v.getTag(R.string.equipo);
            ObjectAnimator anim = ObjectAnimator.ofFloat(v, "rotation", rotationAngle, rotationAngle + 180);
            anim.setDuration(500);
            anim.start();
            rotationAngle += 180;
            rotationAngle = rotationAngle % 360;
            v.setTag(R.string.angle, rotationAngle);


            if (null == holder.expandView.getTag(R.string.expanded)) {
                holder.expandView.setTag(R.string.expanded, false);
            }

            int height = (int) holder.expandView.getTag(R.string.original_height);
            boolean expanded = (boolean) holder.expandView.getTag(R.string.expanded);


            ValueAnimator expandAnimator;
            ValueAnimator colorAnimator;
            AnimatorSet set = new AnimatorSet();
            if (team.isExpanded() && expanded) {
                holder.expandView.setTag(R.string.expanded, false);
                team.setExpanded(false);
                expandAnimator = ValueAnimator.ofInt(height, 0);
                set.play(ObjectAnimator.ofFloat(holder.expandView, View.ALPHA, 1.0f, 0.0f));
                colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), _expandColor, _collapseColor);
            } else {
                holder.expandView.setTag(R.string.expanded, true);
                team.setExpanded(true);
                expandAnimator = ValueAnimator.ofInt(0, height);
                set.play(ObjectAnimator.ofFloat(holder.expandView, View.ALPHA, 0.0f, 1.0f));
                colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), _collapseColor, _expandColor);
            }
            expandAnimator.setDuration(250);
            expandAnimator.setInterpolator(new LinearInterpolator());
            expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    holder.expandView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                    holder.expandView.requestLayout();
                }
            });
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //super.onAnimationEnd(animation);
                    holder.expand.setEnabled(true);
                    holder.expand.setClickable(true);
                }
            });
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    holder.rootView.setBackgroundColor((Integer) animation.getAnimatedValue());
                }
            });
            holder.expand.setEnabled(false);
            holder.expand.setClickable(false);
            set.start();
            colorAnimator.start();
            expandAnimator.start();
        }
    }

    private void bindStatsView(final PositionViewHolder holder, TeamInTournament team) {
        measureView(holder.expandView);
        initViewState(holder, holder.expandView.getMeasuredHeight());
        holder.expand.setOnClickListener(new ExpandView(holder, team));
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private void initViewState(final PositionViewHolder holder, int height) {

        TeamInTournament item = (TeamInTournament) holder.itemView.getTag();

        holder.expandView.setTag(R.string.original_height, height);

        if (null == holder.expandView.getTag(R.string.expanded)) {
            holder.expandView.setTag(R.string.expanded, false);
        }

        boolean expanded = (boolean) holder.expandView.getTag(R.string.expanded);

        AnimatorSet set = new AnimatorSet();
        if (item.isExpanded() && expanded) {
            holder.expandView.setTag(R.string.expanded, true);
            holder.expandView.getLayoutParams().height = height;
            holder.rootView.setBackgroundColor(this._expandColor);
            set.play(ObjectAnimator.ofFloat(holder.expandView, View.ALPHA, 0.0f, 1.0f));
            holder.rootView.requestLayout();
        } else {
            holder.expandView.setTag(R.string.expanded, false);
            set.play(ObjectAnimator.ofFloat(holder.expandView, View.ALPHA, 1.0f, 0.0f));
            holder.expandView.getLayoutParams().height = 0;
            holder.rootView.setBackgroundColor(this._collapseColor);
            holder.rootView.requestLayout();
        }
        set.setDuration(100);
        set.start();
    }

    @Override
    public long getHeaderId(int position) {
        /*
        if (position < 0 || position > getItemCount()) {
            return -1;
        }

        String name = teamInTournaments.get(position).name;
        return (long) name.charAt(name.length() - 1);
        */
        /*

        if (position == 0) {
            return -1;
        } else {
            String name = teamInTournaments.get(position).name;
            return (long) name.charAt(name.length() - 1);
        }
        */
        return (long) 'g';
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_position_table_header, parent, false)) {
        };

    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    private void setItems(List<TeamInTournament> items, String OrderBy, String OrderByDirection) {
        _items = new SortedList<>(TeamInTournament.class, getOrderByCallback(OrderBy, OrderByDirection));

        for (TeamInTournament item : items) {
            _items.add(item);
        }
    }

    private int getCompare(String orderBy, TeamInTournament team1, TeamInTournament team2, String orderByDirection) {
        int result;
        switch (orderBy) {
            case "td":
                if (orderByDirection.equals("-"))
                    result = team2.getTryDiference() > team1.getTryDiference() ? 1 : (team2.getTryDiference() < team1.getTryDiference() ? -1 : 0);
                else
                    result = team1.getTryDiference() > team2.getTryDiference() ? 1 : (team1.getTryDiference() < team2.getTryDiference() ? -1 : 0);
                break;
            case "tf":
                if (orderByDirection.equals("-"))
                    result = team2.getTf() > team1.getTf() ? 1 : (team2.getTf() < team1.getTf() ? -1 : 0);
                else
                    result = team1.getTf() > team2.getTf() ? 1 : (team1.getTf() < team2.getTf() ? -1 : 0);
                break;
            case "tc":
                if (orderByDirection.equals("-"))
                    result = team2.getTc() > team1.getTc() ? 1 : (team2.getTc() < team1.getTc() ? -1 : 0);
                else
                    result = team1.getTc() > team2.getTc() ? 1 : (team1.getTc() < team2.getTc() ? -1 : 0);
                break;
            case "cr":
                if (orderByDirection.equals("-"))
                    result = team2.getCr() > team1.getCr() ? 1 : (team2.getCr() < team1.getCr() ? -1 : 0);
                else
                    result = team1.getCr() > team2.getCr() ? 1 : (team1.getCr() < team2.getCr() ? -1 : 0);
                break;
            case "pr":
                if (orderByDirection.equals("-"))
                    result = team2.getPr() > team1.getPr() ? 1 : (team2.getPr() < team1.getPr() ? -1 : 0);
                else
                    result = team1.getPr() > team2.getPr() ? 1 : (team1.getPr() < team2.getPr() ? -1 : 0);
                break;
            case "gp":
                if (orderByDirection.equals("-"))
                    result = team2.getGp() > team1.getGp() ? 1 : (team2.getGp() < team1.getGp() ? -1 : 0);
                else
                    result = team1.getGp() > team2.getGp() ? 1 : (team1.getGp() < team2.getGp() ? -1 : 0);
                break;
            case "lf":
                if (orderByDirection.equals("-"))
                    result = team2.getLf() > team1.getLf() ? 1 : (team2.getLf() < team1.getLf() ? -1 : 0);
                else
                    result = team1.getLf() > team2.getLf() ? 1 : (team1.getLf() < team2.getLf() ? -1 : 0);
                break;
            case "gw":
                if (orderByDirection.equals("-"))
                    result = team2.getGw() > team1.getGw() ? 1 : (team2.getGw() < team1.getGw() ? -1 : 0);
                else
                    result = team1.getGw() > team2.getGw() ? 1 : (team1.getGw() < team2.getGw() ? -1 : 0);
                break;
            case "gl":
                if (orderByDirection.equals("-"))
                    result = team2.getGl() > team1.getGl() ? 1 : (team2.getGl() < team1.getGl() ? -1 : 0);
                else
                    result = team1.getGl() > team2.getGl() ? 1 : (team1.getGl() < team2.getGl() ? -1 : 0);
                break;
            case "gd":
                if (orderByDirection.equals("-"))
                    result = team2.getGd() > team1.getGd() ? 1 : (team2.getGd() < team1.getGd() ? -1 : 0);
                else
                    result = team1.getGd() > team2.getGd() ? 1 : (team1.getGd() < team2.getGd() ? -1 : 0);
                break;
            case "name":
                if (orderByDirection.equals("-"))
                    result = team2.getTeam().getShortName().compareToIgnoreCase(team1.getTeam().getShortName());
                else
                    result = team1.getTeam().getShortName().compareToIgnoreCase(team2.getTeam().getShortName());
                break;
            case "tp":
            default:
                if (orderByDirection.equals("-"))
                    result = team2.getTp() > team1.getTp() ? 1 : (team2.getTp() < team1.getTp() ? -1 : 0);
                else
                    result = team1.getTp() > team2.getTp() ? 1 : (team1.getTp() < team2.getTp() ? -1 : 0);
                break;
        }
        return result;
    }

    private boolean getAreContentsTheSame(String orderBy, TeamInTournament team1, TeamInTournament team2) {
        boolean result;
        switch (orderBy) {
            case "td":
                result = team1.getTryDiference().equals(team2.getTryDiference());
                break;
            case "tf":
                result = team1.getTf().equals(team2.getTf());
                break;
            case "tc":
                result = team1.getTc().equals(team2.getTc());
                break;
            case "cr":
                result = team1.getCr().equals(team2.getCr());
                break;
            case "pr":
                result = team1.getPr().equals(team2.getPr());
                break;
            case "gp":
                result = team1.getGp().equals(team2.getGp());
                break;
            case "lf":
                result = team1.getLf().equals(team2.getLf());
                break;
            case "gw":
                result = team1.getGw().equals(team2.getGw());
                break;
            case "gl":
                result = team1.getGl().equals(team2.getGl());
                break;
            case "gd":
                result = team1.getGd().equals(team2.getGd());
                break;
            case "name":
                result = team1.getTeam().getShortName().equalsIgnoreCase(team2.getTeam().getShortName());
                break;
            case "tp":
            default:
                result = team1.getTp().equals(team2.getTp());
        }
        return result;
    }

    private SortedListAdapterCallback<TeamInTournament> getOrderByCallback(final String orderBy, final String orderByDirection) {
        return new SortedListAdapterCallback<TeamInTournament>(PositionRecyclerAdapter.this) {
            @Override
            public int compare(TeamInTournament team1, TeamInTournament team2) {
                return getCompare(orderBy, team1, team2, orderByDirection);
            }

            @Override
            public boolean areContentsTheSame(TeamInTournament oldItem, TeamInTournament newItem) {
                return getAreContentsTheSame(orderBy, oldItem, newItem);
            }

            @Override
            public boolean areItemsTheSame(TeamInTournament item1, TeamInTournament item2) {
                return item1.getId().equals(item2.getId());
            }
        };
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }

    @Override
    public TeamInTournament getItem(int position) {
        return _items.get(position);
    }

    public void replaceData(List<TeamInTournament> items) {
        setItems(items);
        notifyDataSetChanged();
    }

    public void reOrderDataBy(List<TeamInTournament> items, String OrderBy, String OrderByDirection) {
        setItems(items, OrderBy, OrderByDirection);
        notifyDataSetChanged();
    }

    private class PositionViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView teamName;
        TextView gpe;
        TextView pts;
        TextView tries;
        TextView penals;
        TextView puntos;
        ImageView logo;
        ImageButton expand;
        View expandView;

        PositionViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            logo = (ImageView) itemView.findViewById(R.id.logo);
            teamName = (TextView) itemView.findViewById(R.id.teamName);
            gpe = (TextView) itemView.findViewById(R.id.gpe);
            pts = (TextView) itemView.findViewById(R.id.pts);
            expand = (ImageButton) itemView.findViewById(R.id.expand);
            expandView = itemView.findViewById(R.id.expandLayout);

            tries = (TextView) itemView.findViewById(R.id.tries);
            penals = (TextView) itemView.findViewById(R.id.penals);
        }

        void render(RecyclerView.ViewHolder holder, TeamInTournament teamInTournament) {

            teamName.setText(teamInTournament.getTeam().getShortName());
            gpe.setText(String.valueOf(teamInTournament.getGame()));
            pts.setText(String.valueOf(teamInTournament.getTp()));
            tries.setText(String.valueOf(teamInTournament.getTries()));
            penals.setText(String.valueOf(teamInTournament.getPenalsAndConversions()));

            if (teamInTournament.getTeam().getLogo() != null) {
                Picasso.with(_context)
                        .load(teamInTournament.getTeam().getLogo().getThumb("120", "120"))
                        .into(logo, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {

                            }
                        });
            } else {
                logo.setVisibility(View.INVISIBLE);
            }

            expand.setTag(R.string.equipo, teamInTournament);
            expand.setTag(R.string.angle, 0);
            itemView.setTag(teamInTournament);
            bindStatsView((PositionViewHolder) holder, teamInTournament);
        }
    }
}
