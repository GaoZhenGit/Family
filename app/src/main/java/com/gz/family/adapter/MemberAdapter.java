package com.gz.family.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gz.family.R;
import com.gz.family.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by host on 2016/2/27.
 */
public class MemberAdapter extends BaseAdapter {
    List<User> list;

    public MemberAdapter(List<User> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        User memberItem = list.get(position);
        Picasso.with(convertView.getContext())
                .load(memberItem.getAvatar())
                .error(R.mipmap.default_avatar)
                .into(viewHolder.avatar);
        viewHolder.Name.setText(memberItem.getName() + "");
        viewHolder.Detail.setText(memberItem.getDetail() + "");
        viewHolder.Tag.setVisibility(View.GONE);
        return convertView;
    }

    public final class ViewHolder {
        public ImageView avatar;
        public TextView Name;
        public TextView Detail;
        public TextView Tag;
    }
}
