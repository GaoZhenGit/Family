package com.gz.family.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.gz.family.application.ActivityManager;

/**
 * Created by host on 2015/8/18.
 */
public abstract class BasePageActivity extends FragmentActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        mContext=this;
        initData();
        initLayoutView();
        initView();
        setListener();
    }

    protected abstract void initData();
    protected abstract void initLayoutView();
    protected abstract void initView();
    protected abstract void setListener();
    public void ShowToast(String string){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    public void ShowToastLong(String string){
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    public void postRedirectToActivity(final Class<?> targetActivity, int postTime) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), targetActivity);
                startActivity(intent);
                finish();
            }
        }, postTime);
    }
}
