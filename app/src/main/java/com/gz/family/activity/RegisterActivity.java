package com.gz.family.activity;


import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.gz.family.Network.Net;
import com.gz.family.R;
import com.gz.family.application.Constant;
import com.gz.family.model.User;
import com.gz.family.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BasePageActivity {
    private AQuery aq;
    private EditText phone_et;
    private EditText name_et;
    private EditText password_et;
    private EditText address_et;
    private EditText age_et;
    private CheckBox cmale;
    private CheckBox cfemale;

    @Override
    protected void initData() {

    }

    @Override
    protected void initLayoutView() {
        setContentView(R.layout.activity_register);
        aq = new AQuery(this);
    }

    @Override
    protected void initView() {
        aq.id(R.id.title_left_img).visible();
        aq.id(R.id.title_mid_text).text("注册用户");
        phone_et = (EditText) findViewById(R.id.login_phone_et);
        name_et = (EditText) findViewById(R.id.login_name_et);
        password_et = (EditText) findViewById(R.id.login_password_et);
        address_et = (EditText) findViewById(R.id.login_address_et);
        age_et = (EditText) findViewById(R.id.login_age_et);
        cmale = (CheckBox) findViewById(R.id.login_check_male);
        cfemale = (CheckBox) findViewById(R.id.login_check_female);
    }

    @Override
    protected void setListener() {
        aq.id(R.id.title_left_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        aq.id(R.id.regist_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> param = new HashMap<>();
                param.put("phone", phone_et.getText().toString());
                param.put("name", name_et.getText().toString());
                param.put("password", password_et.getText().toString());
                param.put("address", address_et.getText().toString());
                param.put("age",age_et.getText().toString());
                if (cmale.isChecked()) {
                    param.put("sex", User.MALE + "");
                } else {
                    param.put("sex", User.FEMALE + "");
                }
                Net.post(Constant.URL.Register, param, new Net.NetworkListener() {
                    @Override
                    public void onSuccess(String response) {
                        LogUtil.i("registerAct", "reg" + response);
                    }

                    @Override
                    public void onFail(String error) {
                        LogUtil.i("registerAct", "err" + error);
                    }
                });
            }
        });
        aq.id(R.id.login_check_male).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmale.setChecked(true);
                cfemale.setChecked(false);
            }
        });
        aq.id(R.id.login_check_female).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmale.setChecked(false);
                cfemale.setChecked(true);
            }
        });
    }
}
