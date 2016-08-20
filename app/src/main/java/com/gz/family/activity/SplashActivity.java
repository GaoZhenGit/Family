package com.gz.family.activity;


import android.os.Bundle;

import com.androidquery.AQuery;
import com.gz.family.R;
import com.gz.family.dataBase.UserStore;
import com.gz.family.model.User;


public class SplashActivity extends BasePageActivity {
    private AQuery aq;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initLayoutView() {
        setContentView(R.layout.activity_splash);
        if (UserStore.getLocalUser(true) == null) {
            postRedirectToActivity(LoginActivity.class, 3000);
        } else {
            postRedirectToActivity(MainActivity.class,1000);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }




}
