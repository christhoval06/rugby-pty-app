package app.christhoval.rugbypty.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.models.Message;

/**
 * Created by christhoval on 08/09/16.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> mMessages;
    private int[] mUsernameColors;
    private String currentUser;

    public MessageAdapter(List<Message> messages) {
        mMessages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = -1;
        switch (viewType) {
            case Message.TYPE_MESSAGE:
                layout = R.layout.view_item_message;
                break;
            case Message.TYPE_LOG:
                layout = R.layout.view_item_log;
                break;
            case Message.TYPE_ACTION:
                layout = R.layout.view_item_action;
                break;
        }

        mUsernameColors = parent.getContext().getResources().getIntArray(R.array.username_colors);

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.setMessage(message.getMessage());
        viewHolder.setImage(message.getImage());
        setMessageAuthor(viewHolder, message);
        setIncomingOrOutgoingMessageAttributes(viewHolder, message);
        setMessageInfo(viewHolder, message);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mMessageView;
        private TextView mUsernameView;
        private TextView messageInfoView;
        private LinearLayout chatMessageContainer;
        private RelativeLayout messageContentContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            chatMessageContainer = (LinearLayout) itemView.findViewById(R.id.chat_message_container);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            messageInfoView = (TextView) itemView.findViewById(R.id.message_info);
            messageContentContainer = (RelativeLayout) itemView.findViewById(R.id.message_content_container);
        }

        public void setMessage(String message) {
            if (null == mMessageView) return;
            if (null == message) return;
            mMessageView.setText(message);
        }

        public void setImage(Bitmap bmp) {
            if (null == mImageView) return;
            if (null == bmp) return;
            mImageView.setImageBitmap(bmp);
        }

        public void setUser(String username) {
            if (null == mUsernameView) return;
            if (null == username) return;
            mUsernameView.setText(username);
            mUsernameView.setTextColor(getUsernameColor(username));
        }

        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }

        public void setInfo(String info) {
            if (null == messageInfoView) return;
            if (null == info) return;
            messageInfoView.setText(info);
            messageInfoView.setVisibility(View.VISIBLE);
        }
    }

    public void addItem(Message message) {
        addItemInBottom(message);
    }

    public void addItemInTop(Message message) {
        mMessages.add(0, message);
        notifyItemInserted(0);
    }

    public void addItemInBottom(Message message) {
        mMessages.add(message);
        notifyItemInserted(mMessages.size() - 1);
    }

    public void setCurrentUser(String username) {
        currentUser = username;
    }

    private void setMessageInfo(ViewHolder holder, Message chatMessage) {
        if (Message.TYPE_MESSAGE == chatMessage.getType()) {
            holder.setInfo(chatMessage.getTime());
        }
    }

    private void setMessageAuthor(ViewHolder holder, Message chatMessage) {
        if (Message.TYPE_MESSAGE == chatMessage.getType()) {
            if (isIncoming(chatMessage)) {
                holder.setUser(chatMessage.getUsername());
            } else {
                holder.mUsernameView.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    private void setIncomingOrOutgoingMessageAttributes(ViewHolder holder, Message chatMessage) {
        if (Message.TYPE_MESSAGE == chatMessage.getType()) {
            boolean isIncoming = isIncoming(chatMessage);
            int gravity = isIncoming ? Gravity.RIGHT : Gravity.LEFT;
            holder.chatMessageContainer.setGravity(gravity);
            holder.messageInfoView.setGravity(gravity);

            int messageBodyContainerBgResource = isIncoming ? R.drawable.incoming_message_bg : R.drawable.outgoing_message_bg;

        /*
        if (hasAttachments(chatMessage)) {
            holder.messageBodyContainerLayout.setBackgroundResource(0);
            holder.messageBodyContainerLayout.setPadding(0, 0, 0, 0);
            holder.attachmentImageView.setMaskResourceId(messageBodyContainerBgResource);
        } else {
        }
        */

            holder.messageContentContainer.setBackgroundResource(messageBodyContainerBgResource);

        /*
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.mUsernameView.getLayoutParams();
        if (isIncoming && hasAttachments(chatMessage)) {
            lp.leftMargin = ResourceUtils.getDimen(R.dimen.chat_message_attachment_username_margin);
            lp.topMargin = ResourceUtils.getDimen(R.dimen.chat_message_attachment_username_margin);
        } else if (isIncoming) {
            lp.leftMargin = holder.messageInfoView.getContext().getDimen(R.dimen.chat_message_username_margin);
            lp.topMargin = 0;
        //}
        /*
        holder.mUsernameView.setLayoutParams(lp);

        int textColorResource = isIncoming
                ? R.color.text_color_black
                : R.color.text_color_white;
        holder.messageBodyTextView.setTextColor(ResourceUtils.getColor(textColorResource));
        */

            /*
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.messageInfoView.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            lp.addRule(isIncoming ? RelativeLayout.ALIGN_PARENT_LEFT : RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            holder.messageInfoView.setLayoutParams(lp);
            */
        }
    }

    private boolean isIncoming(Message chatMessage) {
        return chatMessage.getUsername() != null && !chatMessage.getUsername().equals(currentUser);
    }
}
