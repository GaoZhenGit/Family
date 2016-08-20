package com.gz.family.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gz.family.Network.Net;
import com.gz.family.R;
import com.gz.family.dataBase.UserStore;
import com.gz.family.model.User;
import com.gz.family.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BasePageActivity {
    private AQuery aq;
    private EditText phone_et;
    private EditText password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initLayoutView() {
        setContentView(R.layout.activity_login);
        aq = new AQuery(this);
    }

    @Override
    protected void initView() {
        aq.id(R.id.title_left_img).visible();
        aq.id(R.id.title_mid_text).text("登录");
        phone_et = (EditText) findViewById(R.id.et_phone);
        password_et = (EditText) findViewById(R.id.et_password);
    }

    @Override
    protected void setListener() {
        aq.id(R.id.title_left_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        aq.id(R.id.login_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = phone_et.getText().toString();
                String password = password_et.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                    return;
                }
                Map<String, String> param = new HashMap<>();
                param.put("phone", name);
                param.put("password", password);
                Net.login(param, new Net.NetworkListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.optInt("state");
                            if (state == 1) {
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                                ShowToast("登录成功");
                                String userS = jsonObject.optString("user");
                                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                                User me = gson.fromJson(userS,User.class);
                                UserStore.storeLocalUser(me);
                                finish();
                            } else {
                                ShowToast("密码错误");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ShowToast("登录失败");
                        }
                    }

                    @Override
                    public void onFail(String error) {
                        ShowToast("登录失败");
                    }
                });
            }
        });
        aq.id(R.id.btn_register).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
            }
        });
    }


}

