package com.gz.family.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * base fragment
 * Created by host on 2015/11/5.
 */
public abstract class BaseFragment extends Fragment {
    protected View convertView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setConvertView(inflater, container);
        fetchData();
        initView();
        setListener();
        return convertView;
    }

    //设置总界面，用contextView =inflater.inflate(R.layout.xxx,container,false);
    abstract void setConvertView(LayoutInflater inflater, ViewGroup container);
    //获取数据
    abstract void fetchData();
    //页面细节初始化
    public abstract void initView();
    //设置监听器
    abstract void setListener();

    //不用强制类型转换的findviewbyid
    protected <E> E findView(int id) {
        return (E) convertView.findViewById(id);
    }

    //直接输出toast
    protected void toast(String string){
        Toast.makeText(this.getContext(), string, Toast.LENGTH_SHORT).show();
    }
}
