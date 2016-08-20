package com.gz.family.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.gz.family.Mqtt.Log;
import com.gz.family.Network.Net;
import com.gz.family.R;
import com.gz.family.application.Constant;
import com.gz.family.model.ChatMessage;
import com.gz.family.model.User;
import com.gz.family.utils.LogUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by host on 2016/3/1.
 */
public class InvitationActivity extends BasePageActivity {
    ChatMessage systemMsg;
    AQuery aQuery;

    TextView relation;
    TextView invitation;
    Button agree;
    Button disagree;

    String call;
    String beCalled;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }
        systemMsg = (ChatMessage) bundle.getSerializable("system_message");
    }

    @Override
    protected void initLayoutView() {
        setContentView(R.layout.activity_invitation);
        aQuery = new AQuery(this);
    }

    @Override
    protected void initView() {
        relation = (TextView) findViewById(R.id.relation_et);
        invitation = (TextView) findViewById(R.id.invitation_tv);
        agree = (Button) findViewById(R.id.agree_btn);
        disagree = (Button) findViewById(R.id.disagree_btn);

        if (systemMsg.getOwner() != null) {
            User selectMember = systemMsg.getOwner();
            aQuery.id(R.id.title_mid_text).text("详细资料");
            aQuery.id(R.id.title_left_img).visible();

            aQuery.id(R.id.me_name).text(selectMember.getName());
            aQuery.id(R.id.me_detail).text(selectMember.getDetail());
            Picasso.with(this).load(selectMember.getAvatar())
                    .into((ImageView) findViewById(R.id.me_avatar));
            if (selectMember.getSex() == User.MALE) {
                aQuery.id(R.id.me_sex).image(R.mipmap.male);
            } else {
                aQuery.id(R.id.me_sex).image(R.mipmap.female);
            }
        } else {
            findViewById(R.id.member_bar).setVisibility(View.GONE);
        }

        if (systemMsg.getText() != null) {
            try {
                JSONObject jsonObject = new JSONObject(systemMsg.getText());
                String relationString = jsonObject.optString("relation");
                String invitationString = jsonObject.optString("invitation");
                invitation.setText(invitationString);
                String[] sub = relationString.split("\\/");
                if (sub.length != 2) {
                    finish();
                    return;
                }
                String callString[] = new String[2];
                //更改直系关系代号
                if (sub[0].startsWith("%") && sub[1].startsWith("%")) {
                    if (sub[0].equals("%father")) {
                        callString[0] = "父亲";
                    }
                    if (sub[0].equals("%mother")) {
                        callString[0] = "母亲";
                    }
                    if (sub[0].equals("%son")) {
                        callString[0] = "儿子";
                    }
                    if (sub[0].equals("%daughter")) {
                        callString[0] = "女儿";
                    }
                    if (sub[0].equals("%wife")) {
                        callString[0] = "妻子";
                    }
                    if (sub[0].equals("%husbund")) {
                        callString[0] = "丈夫";
                    }

                    if (sub[1].equals("%father")) {
                        callString[1] = "父亲";
                    }
                    if (sub[1].equals("%mother")) {
                        callString[1] = "母亲";
                    }
                    if (sub[1].equals("%son")) {
                        callString[1] = "儿子";
                    }
                    if (sub[1].equals("%daughter")) {
                        callString[1] = "女儿";
                    }
                    if (sub[1].equals("%wife")) {
                        callString[1] = "妻子";
                    }
                    if (sub[1].equals("%husbund")) {
                        callString[1] = "丈夫";
                    }
                }
                relation.setText("关系：您的" + callString[1] + "（" + callString[0] + "）");
                call = sub[1];
                beCalled = sub[0];
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void setListener() {
        aQuery.id(R.id.title_left_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> param = new HashMap<>();
                param.put("type", "add");
                param.put("phone", systemMsg.getOwner().getPhone());
                param.put("relation", call);
                param.put("beRelation", beCalled);
                Net.post(Constant.URL.Relatives, param, new Net.NetworkListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.optInt("state");
                            if (state == 1) {
                                ShowToast("添加成功");
                                finish();
                            } else {
                                onFail("1");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onFail("2");
                        }
                    }

                    @Override
                    public void onFail(String error) {
                        ShowToast("操作失败" + error);
                    }
                });
            }
        });
        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
