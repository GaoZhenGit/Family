package com.gz.family.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.gz.family.Network.Net;
import com.gz.family.R;
import com.gz.family.activity.MemberInfoActivity;
import com.gz.family.adapter.MemberTagAdapter;
import com.gz.family.application.Constant;
import com.gz.family.dataBase.UserStore;
import com.gz.family.model.User;
import com.gz.family.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by host on 2016/1/30.
 */
public class FamilyTabFragment extends BaseFragment {

    private MemberTagAdapter memberAdapter;

    private ListView listView;

    private User me;

    private List<Map.Entry<String,User>> relatives;

    @Override
    void setConvertView(LayoutInflater inflater, ViewGroup container) {
        convertView = inflater.inflate(R.layout.fragment_tab_family, container, false);
    }

    @Override
    void fetchData() {
        me = UserStore.getLocalUser(false);
        memberAdapter = new MemberTagAdapter(me);
        relatives = new ArrayList<>();
        for (Map.Entry<String,User> r:me.getRelatives().entrySet()) {
            relatives.add(r);
        }
    }

    @Override
    public void initView() {
        listView = (ListView) convertView.findViewById(R.id.family_listview);
        listView.setAdapter(memberAdapter);
    }

    @Override
    void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectMember = relatives.get(position).getValue();
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectMember", selectMember);
                Intent intent = new Intent(getContext(), MemberInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void refreshData() {
        Net.get(Constant.URL.Login, null, new Net.NetworkListener() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                User newMe = gson.fromJson(response, User.class);
                User oldMe = UserStore.getLocalUser(false);
                if(!newMe.getRelatives().equals(oldMe.getRelatives())) {
                    UserStore.storeLocalUser(newMe);
                    fetchData();
                    listView.setAdapter(memberAdapter);
                    LogUtil.i("refreshData");
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
        User me = UserStore.getLocalUser(false);

    }
}
