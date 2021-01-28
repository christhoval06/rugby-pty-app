package app.christhoval.rugbypty.mvp.notifications;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.models.Notification;

/**
 * Adaptador de notificaciones
 */
public class NotificationsAdapter
        extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    ArrayList<Notification> notifications = new ArrayList<>();

    public NotificationsAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.view_notification_match_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        /*
        holder.title.setText(newNotification.getTitle());
        holder.description.setText(newNotification.getDescription());
        holder.expiryDate.setText(String.format("Válido hasta el %s", newNotification.getDate()));
        holder.discount.setText(String.format("%d%%", (int) (newNotification.getPoints())));
        */

        if (notification.isWasPlayed()) {
            holder.score.setText(String.format("%1$s - %2$s", notification.getTeamHomePoints(), notification.getTeamAwayPoints()));
            holder.playedTime.setText(notification.getTime());
        } else {
            holder.PlayedView.setVisibility(View.GONE);
            holder.noPlayedView.setVisibility(View.VISIBLE);
            holder.startDate.setText(notification.getDate());
            holder.startTime.setText(notification.getTime());
        }

        holder.teamAName.setText(notification.getTeamHomeName());
        if (notification.getTeamHomeFlag() != null) {
            Picasso.with(holder.rootView.getContext())
                    .load(notification.getTeamHomeFlag())
                    .into(holder.teamALogo, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            holder.teamALogo.setVisibility(View.INVISIBLE);
        }

        holder.teamBName.setText(notification.getTeamAwayName());
        if (notification.getTeamAwayFlag() != null) {
            Picasso.with(holder.rootView.getContext())
                    .load(notification.getTeamAwayFlag())
                    .into(holder.teamBLogo, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            holder.teamBLogo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void replaceData(ArrayList<Notification> items) {
        setList(items);
        notifyDataSetChanged();
    }

    public void setList(ArrayList<Notification> list) {
        this.notifications = list;
    }

    public void addItem(Notification pushMessage) {
        notifications.add(0, pushMessage);
        notifyItemInserted(0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public ImageView teamALogo;
        public TextView teamAName;
        public ImageView teamBLogo;
        public TextView teamBName;
        public TextView startDate;
        public TextView startTime;
        public TextView playedTime;
        public TextView score;

        public View noPlayedView;
        public View PlayedView;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;

            teamALogo = (ImageView) itemView.findViewById(R.id.teamALogo);
            teamAName = (TextView) itemView.findViewById(R.id.teamAName);
            teamBLogo = (ImageView) itemView.findViewById(R.id.teamBLogo);
            teamBName = (TextView) itemView.findViewById(R.id.teamBName);

            score = (TextView) itemView.findViewById(R.id.score);
            startDate = (TextView) itemView.findViewById(R.id.startDate);
            startTime = (TextView) itemView.findViewById(R.id.starTime);
            playedTime = (TextView) itemView.findViewById(R.id.playedTime);

            noPlayedView = itemView.findViewById(R.id.noPlayed);
            PlayedView = itemView.findViewById(R.id.wasPlayed);
        }
    }
}
