package app.christhoval.rugbypty.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.adapters.base.BaseRecyclerAdapter;
import app.christhoval.rugbypty.models.Match;
import app.christhoval.rugbypty.models.MatchDay;
import app.christhoval.rugbypty.utilities.RugbyPTYAPIHelpers;
import app.christhoval.rugbypty.utilities.Utils;

/**
 * Created by christhoval
 * on 08/19/16.
 */
public class FixtureHeadersRecyclerAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder, Match> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private Context context;

    public void replaceData(List<MatchDay> items, String direction) {
        setItems(RugbyPTYAPIHelpers.convertMatchDays2Matches(items, direction));
        notifyDataSetChanged();
    }

    private void setItems(List<MatchDay> items, String team, String direction) {
        setItems(RugbyPTYAPIHelpers.convertMatchDays2Matches(items, direction, team));
        notifyDataSetChanged();
    }

    public void filter(List<MatchDay> items, String team, String direction) {
        setItems(items, team, direction);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = getLayoutInflater(parent).inflate(R.layout.view_item_fixture, parent, false);
        this.context = parent.getContext();
        return new FixturesViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Match match = getItem(position);
        FixturesViewHolder fixturesViewHolder = (FixturesViewHolder) holder;
        fixturesViewHolder.render(match);
        fixturesViewHolder.setIsRecyclable(false);
    }

    @Override
    public long getHeaderId(int position) {
        if (position < 0 || position > getItemCount()) {
            return -1;
        }
        return getItem(position).getMatchDay().getMilliSeconds();
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new RecyclerView.ViewHolder(getLayoutInflater(parent).inflate(R.layout.view_item_fixtures_header, parent, false)) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        Match match = getItem(position);
        View container = holder.itemView.findViewById(R.id.container);
        container.setBackgroundColor(Utils.getColor(context, match.getMatchDay().isDateGreatThanNow() ? R.color.foursquare : R.color.skype));
        TextView date = (TextView) holder.itemView.findViewById(R.id.date);
        date.setText(String.format("%1$s", match.getMatchDay().getMatchDateWithFormat("dd 'de' MMMM 'de' yyyy")));
    }

    private class FixturesViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        ImageView teamALogo;
        TextView teamAName;
        ImageView teamBLogo;
        TextView teamBName;
        TextView startDate;
        TextView startTime;
        TextView playedTime;
        TextView score;
        TextView estadio;

        View noPlayedView;
        View PlayedView;


        FixturesViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;

            teamALogo = (ImageView) itemView.findViewById(R.id.teamALogo);
            teamAName = (TextView) itemView.findViewById(R.id.teamAName);
            teamBLogo = (ImageView) itemView.findViewById(R.id.teamBLogo);
            teamBName = (TextView) itemView.findViewById(R.id.teamBName);

            score = (TextView) itemView.findViewById(R.id.score);
            estadio = (TextView) itemView.findViewById(R.id.estadio);
            startDate = (TextView) itemView.findViewById(R.id.startDate);
            startTime = (TextView) itemView.findViewById(R.id.starTime);
            playedTime = (TextView) itemView.findViewById(R.id.playedTime);

            noPlayedView = itemView.findViewById(R.id.noPlayed);
            PlayedView = itemView.findViewById(R.id.wasPlayed);
        }

        void render(Match match) {
            if (match.isWasPlayed()) {
                score.setText(String.format("%1$s - %2$s", match.getTeamAPoints(), match.getTeamBPoints()));
                playedTime.setText(match.getMatchDateWithFormat("hh:mm  a"));
            } else {
                PlayedView.setVisibility(View.GONE);
                noPlayedView.setVisibility(View.VISIBLE);
                startDate.setText(match.getMatchDateWithFormat("dd MMM"));
                startTime.setText(match.getMatchDateWithFormat("hh:mm  a"));
                estadio.setText(match.getMatchDay().getPlace());
            }

            String LOGO_W = "320";
            if (match.getTeamA().getTeam() != null) {
                teamAName.setText(match.getTeamA().getTeamRef().getShortName());
                if (match.getTeamA().getTeamRef().getLogo() != null) {
                    Picasso.with(context)
                            .load(match.getTeamA().getTeamRef().getLogo().getThumb(LOGO_W, LOGO_W))
                            .into(teamALogo, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError() {

                                }
                            });
                } else {
                    teamALogo.setVisibility(View.INVISIBLE);
                }
            }

            if (match.getTeamB().getTeam() != null) {
                teamBName.setText(match.getTeamB().getTeamRef().getShortName());
                if (match.getTeamB().getTeamRef().getLogo() != null) {
                    Picasso.with(context)
                            .load(match.getTeamB().getTeamRef().getLogo().getThumb(LOGO_W, LOGO_W))
                            .into(teamBLogo, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError() {

                                }
                            });
                } else {
                    teamBLogo.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
