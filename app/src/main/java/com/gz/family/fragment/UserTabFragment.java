package com.gz.family.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gz.family.R;
import com.gz.family.dataBase.UserStore;
import com.gz.family.model.User;
import com.squareup.picasso.Picasso;

/**
 * Created by host on 2016/1/29.
 */
public class UserTabFragment extends BaseFragment {
    User me;
    ImageView avatar;
    TextView name;
    TextView detail;
    ImageView sex;
    @Override
    void setConvertView(LayoutInflater inflater, ViewGroup container) {
        convertView = inflater.inflate(R.layout.fragment_tab_user, container, false);
    }

    @Override
    void fetchData() {
        me = UserStore.getLocalUser(true);
        avatar = (ImageView) convertView.findViewById(R.id.me_avatar);
        name = (TextView) convertView.findViewById(R.id.me_name);
        detail = (TextView) convertView.findViewById(R.id.me_detail);
        sex = (ImageView) convertView.findViewById(R.id.me_sex);
    }

    @Override
    public void initView() {
        Picasso.with(getContext()).load(me.getAvatar()).into(avatar);
        name.setText(me.getName());
        detail.setText(me.getDetail());
        if(me.getSex() == User.MALE) {
            sex.setImageResource(R.mipmap.male);
        } else {
            sex.setImageResource(R.mipmap.female);
        }
    }

    @Override
    void setListener() {

    }
}
