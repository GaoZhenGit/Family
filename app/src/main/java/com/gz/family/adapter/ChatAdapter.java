package com.gz.family.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.gz.family.R;
import com.gz.family.model.ChatMessage;
import com.gz.family.model.User;
import com.gz.family.utils.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by host on 2016/2/6.
 */
public class ChatAdapter extends BaseAdapter {
    public static final int ME = ChatMessage.ME;
    public static final int OTHER = ChatMessage.OTHER;
    public static final int SYSTEM = ChatMessage.SYSTEM;


    private List<ChatMessage> messageList;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messageList = messages;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getType();
    }

    @Override
    public int getCount() {
        return messageList == null ? 0 : messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler;
        if (convertView == null) {
            viewHoler = new ViewHoler();
            switch (getItemViewType(position)) {
                case ME:
                    convertView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.adapter_chat_right, parent, false);
                    break;
                case OTHER:
                    convertView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.adapter_chat_left, parent, false);
                    break;
                case SYSTEM:
                default:
                    break;
            }
            viewHoler.avator = (ImageView) convertView.findViewById(R.id.img_avator);
            viewHoler.text = (TextView) convertView.findViewById(R.id.tv_chat);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        ChatMessage message = messageList.get(position);
        User who = message.getOwner();
        viewHoler.text.setText(message.getText());
        Picasso.with(viewHoler.avator.getContext()).load(who.getAvatar()).error(R.mipmap.default_avatar).into(viewHoler.avator);
//        LogUtil.i("chatAdp",who.getAvatar());
        return convertView;
    }

    public class ViewHoler {
        public ImageView avator;
        public TextView text;
    }
}
