package com.gz.family.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.gz.family.Mqtt.Log;
import com.gz.family.Network.Net;
import com.gz.family.R;
import com.gz.family.adapter.MemberAdapter;
import com.gz.family.application.Constant;
import com.gz.family.dataBase.UserStore;
import com.gz.family.model.ChatMessage;
import com.gz.family.model.User;
import com.gz.family.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by host on 2016/2/5.
 */
public class SearchFamilyActivity extends BasePageActivity {
    AQuery aQuery;
    EditText searchEditText;
    ProgressBar progressBar;
    ListView listview;
    MemberAdapter adapter;
    List<User> searchList;
    Gson gson;

    @Override
    protected void initData() {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    @Override
    protected void initLayoutView() {
        setContentView(R.layout.activity_search_family);
        aQuery = new AQuery(this);
    }

    @Override
    protected void initView() {
        searchEditText = (EditText) findViewById(R.id.et_search);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        listview = (ListView) findViewById(R.id.search_listview);

        searchList = new ArrayList<>();
        adapter = new MemberAdapter(searchList);
        listview.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        //退出按键
        aQuery.id(R.id.title_left_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //搜索按键
        aQuery.id(R.id.search_button).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = searchEditText.getText().toString();
                if (TextUtils.isEmpty(key)) {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                Map<String, String> param = new HashMap<String, String>();
                param.put("type", "search");
                param.put("key", key);
                Net.post(Constant.URL.Relatives, param, new Net.NetworkListener() {
                    @Override
                    public void onSuccess(String response) {
                        LogUtil.i("search", response);
                        List<User> userList = gson.fromJson(response, new TypeToken<List<User>>() {
                        }.getType());
                        searchList.clear();
                        searchList.addAll(userList);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail(String error) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        //搜索项按键
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                View dialog = getLayoutInflater().inflate(R.layout.alertdialog_invitation, null);

                final TextView invitation = (TextView) dialog.findViewById(R.id.invitation_talk_et);
                final TextView relation = (TextView) dialog.findViewById(R.id.relation_et);
                final TextView beRelation = (TextView) dialog.findViewById(R.id.be_relation_et);
                final LinearLayout rel = (LinearLayout) dialog.findViewById(R.id.ll_relation);
                final LinearLayout beRel = (LinearLayout) dialog.findViewById(R.id.ll_be_relation);
                final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner_relation);

                //确定双方性别
                final int meSex = UserStore.getLocalUser(true).getSex();
                final int othSex = searchList.get(position).getSex();
                //设置下拉选择框(根据性别)
                List<String> list = new ArrayList<>();
                if (othSex == User.MALE) {
                    list.add("父亲");
                    list.add("丈夫");
                    list.add("儿子");
                } else {
                    list.add("母亲");
                    list.add("妻子");
                    list.add("女儿");
                }
                list.add("其他");
                ArrayAdapter<String> select = new ArrayAdapter<>
                        (SearchFamilyActivity.this, android.R.layout.simple_spinner_item, list);
                select.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(select);
                //选择“其他”的时候才显示关系填写栏
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 3) {
                            rel.setVisibility(View.VISIBLE);
                            beRel.setVisibility(View.VISIBLE);
                        } else {
                            rel.setVisibility(View.GONE);
                            beRel.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //弹出框总设置
                AlertDialog alertDialog = new AlertDialog.Builder(SearchFamilyActivity.this)
                        .setView(dialog)
                        .setPositiveButton("邀请添加好友", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ChatMessage chatMessage = new ChatMessage();
                                User me = UserStore.getLocalUser(true);
                                me.setRelatives(null);
                                chatMessage.setOwner(me);
                                chatMessage.setToWhom(searchList.get(position));
                                chatMessage.setType(ChatMessage.SYSTEM);
                                JSONObject text = new JSONObject();
                                try {
                                    text.put("type","invitation");
                                    text.put("invitation", invitation.getText().toString());
                                    if (spinner.getSelectedItemPosition() == 3) {
                                        text.put("relation", relation.getText().toString()
                                                + "/" + beRelation.getText().toString());
                                    } else {
                                        //自动补全直系或者配偶性别关系
                                        String[][][] relationShip = {
                                                {
                                                        {"%mother/%daughter", "", "%daughter/%mother"},
                                                        {"%father/%daughter", "%husbund/%wife", "%son/%mother"}
                                                },
                                                {
                                                        {"%mother/%son", "%wife/%husbund", "%daughter/%father"},
                                                        {"%father/%son", "", "%son/%father"}
                                                }
                                        };
                                        text.put("relation", relationShip[meSex][othSex][spinner.getSelectedItemPosition()]);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                chatMessage.setText(text.toString());
                                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                                Map<String, String> param = new HashMap<>();
                                param.put("target", searchList.get(position).getId());
                                LogUtil.i("invitation",gson.toJson(chatMessage));
                                param.put("message", gson.toJson(chatMessage));
                                param.put("qos", "2");
                                Net.post(Constant.URL.MQTT, param, new Net.NetworkListener() {
                                    @Override
                                    public void onSuccess(String response) {
                                        ShowToast("申请已发出");
                                    }

                                    @Override
                                    public void onFail(String error) {
                                        ShowToast("申请发出失败");
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
    }
}
