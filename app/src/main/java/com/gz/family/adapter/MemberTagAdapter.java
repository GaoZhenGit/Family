package com.gz.family.adapter;

import android.content.Entity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.gz.family.R;
import com.gz.family.model.User;
import com.gz.family.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by host on 2016/1/29.
 */
public class MemberTagAdapter extends BaseAdapter {
    private Map<String, User> relatives;
    private List<Map.Entry<String, User>> userList;

    public MemberTagAdapter(User me) {
        this.relatives = me.getRelatives();
        userList = new ArrayList<>();
        for (Map.Entry<String, User> entity : relatives.entrySet()) {
            userList.add(entity);
        }
    }

    @Override
    public int getCount() {
        return userList == null ? 0 : userList.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_member, parent, false);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.member_avater);
            viewHolder.Name = (TextView) convertView.findViewById(R.id.member_name);
            viewHolder.Detail = (TextView) convertView.findViewById(R.id.member_detail);
            viewHolder.Tag = (TextView) convertView.findViewById(R.id.member_tag);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        User memberItem = userList.get(position).getValue();
        Picasso.with(convertView.getContext()).load(memberItem.getAvatar())
                .into(viewHolder.avatar);
        viewHolder.Name.setText(memberItem.getName() + "");
        viewHolder.Detail.setText(memberItem.getDetail() + "");
        String tag = userList.get(position).getKey()+"";
        if(tag.startsWith("%")) {
            if(tag.equals("%father")) {
                viewHolder.Tag.setText("父亲");
            }
            if(tag.equals("%mother")) {
                viewHolder.Tag.setText("母亲");
            }
            if(tag.equals("%son")) {
                viewHolder.Tag.setText("儿子");
            }
            if(tag.equals("%daughter")) {
                viewHolder.Tag.setText("女儿");
            }
            if(tag.equals("%wife")) {
                viewHolder.Tag.setText("妻子");
            }
            if(tag.equals("%husbund")) {
                viewHolder.Tag.setText("丈夫");
            }
        } else {
            viewHolder.Tag.setText(tag);
        }
        return convertView;
    }

    public final class ViewHolder {
        public ImageView avatar;
        public TextView Name;
        public TextView Detail;
        public TextView Tag;
    }
}
